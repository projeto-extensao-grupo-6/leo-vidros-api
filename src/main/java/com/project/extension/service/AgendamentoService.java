package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.RegraNegocioException;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.strategy.agendamento.AgendamentoContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final EnderecoService enderecoService;
    private final FuncionarioService funcionarioService;
    private final StatusService statusService;
    private final AgendamentoContext agendamentoContext;
    private final ServicoService servicoService;
    private final EtapaService etapaService;
    private final LogService logService;

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getFuncionarios() == null || agendamento.getFuncionarios().isEmpty()) {
            throw new RegraNegocioException("É obrigatório informar pelo menos um funcionário responsável pelo agendamento.");
        }

        if (agendamento.getServico() != null && agendamento.getServico().getId() != null) {
            Servico servico = servicoService.buscarPorId(agendamento.getServico().getId());
            agendamento.setServico(servico);
        }

        Agendamento agendamentoProcessado = agendamentoContext.processarAgendamento(agendamento);
        Agendamento agendamentoSalvo = repository.save(agendamentoProcessado);
        String mensagem = String.format("Novo Agendamento ID %d criado com sucesso. Tipo: %s, Data: %s.",
                agendamentoSalvo.getId(),
                agendamentoSalvo.getTipoAgendamento(),
                agendamentoSalvo.getDataAgendamento());
        logService.success(mensagem);
        return agendamentoSalvo;
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        log.debug("Iniciando edição do Agendamento ID {}.", id);
        Agendamento destino = buscarPorId(id);

        atualizarDadosBasicos(destino, origem);
        atualizarEndereco(destino, origem);
        atualizarHorario(destino, origem);
        atualizarStatus(destino, origem);
        atualizarFuncionarios(destino, origem);

        Agendamento atualizado = repository.save(destino);

        String mensagem = String.format("Agendamento ID %d atualizado com sucesso. Novo Status: %s, Data: %s.",
                atualizado.getId(),
                atualizado.getStatusAgendamento() != null ? atualizado.getStatusAgendamento().getNome() : "N/A",
                atualizado.getDataAgendamento());
        logService.info(mensagem);
        return atualizado;
    }

    public void deletar(Integer id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setServico(null);
        agendamento.getFuncionarios().clear();
        agendamento.getAgendamentoProdutos().clear();
        repository.delete(agendamento);
        logService.info(String.format("Agendamento ID %d desvinculado de funcionários (exclusão lógica).", id));
        log.info("Agendamento ID {} desvinculado de funcionários e mantido no histórico.", id);
    }

    public Agendamento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logService.error(String.format("Falha ao buscar: Agendamento com ID %d não encontrado.", id));
                    log.error("Agendamento com ID {} não encontrado", id);
                    return new AgendamentoNaoEncontradoException();
                });
    }

    public List<Agendamento> buscarTodos() {
        List<Agendamento> lista = repository.findAll();
        logService.info(String.format("Busca por todos os agendamentos realizada. Total de registros: %d.", lista.size()));
        return lista;

    }

    public Agendamento editarDadosBasicos(Agendamento origem, Integer id) {
        Agendamento destino = buscarPorId(id);

        destino.setInicioAgendamento(origem.getInicioAgendamento());
        destino.setFimAgendamento(origem.getFimAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setObservacao(origem.getObservacao());

        if (origem.getStatusAgendamento() != null) {
            Status statusAtualizado = statusService.buscarOuCriarPorTipoENome(
                    origem.getStatusAgendamento().getTipo(),
                    origem.getStatusAgendamento().getNome()
            );
            destino.setStatusAgendamento(statusAtualizado);

            if (destino.getStatusAgendamento().getId() != statusAtualizado.getId()) {
                logService.info(String.format("Status do Agendamento ID %d alterado para: %s.",
                        destino.getId(),
                        statusAtualizado.getNome()));
            }
        }

        return repository.save(destino);
    }

    private void atualizarDadosBasicos(Agendamento destino, Agendamento origem) {
        destino.setTipoAgendamento(origem.getTipoAgendamento());
        destino.setInicioAgendamento(origem.getInicioAgendamento());
        destino.setFimAgendamento(origem.getFimAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setObservacao(origem.getObservacao());
        log.trace("Dados básicos do agendamento atualizados.");
    }

    private void atualizarEndereco(Agendamento destino, Agendamento origem) {
        if (origem.getEndereco() != null) {
            Endereco enderecoAtualizado = enderecoService.editar(origem.getEndereco(), origem.getEndereco().getId());
            destino.setEndereco(enderecoAtualizado);
            log.trace("Endereço do agendamento atualizado.");
        }
    }

    private void atualizarStatus(Agendamento destino, Agendamento origem) {
        if (origem.getStatusAgendamento() != null) {
            Status statusAtualizado = statusService.buscarOuCriarPorTipoENome(
                    origem.getStatusAgendamento().getTipo(),
                    origem.getStatusAgendamento().getNome()
            );
            destino.setStatusAgendamento(statusAtualizado);

            if (destino.getStatusAgendamento().getId() != statusAtualizado.getId()) {
                logService.info(String.format("Status do Agendamento ID %d alterado para: %s.",
                        destino.getId(),
                        statusAtualizado.getNome()));
            }
        }
    }

    private void atualizarFuncionarios(Agendamento destino, Agendamento origem) {
        if (origem.getFuncionarios() != null) {
            List<Funcionario> funcionariosValidados = origem.getFuncionarios().stream()
                    .map(this::validarFuncionario)
                    .toList();

            LocalDate data = origem.getDataAgendamento() != null ? origem.getDataAgendamento() : destino.getDataAgendamento();
            LocalTime inicio = origem.getInicioAgendamento() != null ? origem.getInicioAgendamento() : destino.getInicioAgendamento();
            LocalTime fim = origem.getFimAgendamento() != null ? origem.getFimAgendamento() : destino.getFimAgendamento();

            for (Funcionario f : funcionariosValidados) {
                List<Agendamento> conflitos = repository.findConflitos(f.getId(), data, inicio, fim);
                conflitos = conflitos.stream()
                        .filter(a -> !a.getId().equals(destino.getId()))
                        .toList();

                if (!conflitos.isEmpty()) {
                    throw new RegraNegocioException(
                            String.format("Funcionário '%s' possui conflito de horário nesta data e horário.", f.getNome()));
                }
            }

            destino.getFuncionarios().clear();
            destino.getFuncionarios().addAll(funcionariosValidados);
        }
    }

    private Funcionario validarFuncionario(Funcionario f) {
        if (f.getId() != null) {
            return funcionarioService.buscarPorId(f.getId());
        }

        Funcionario funcionarioSalvo = funcionarioService.buscarPorTelefone(f.getTelefone());
        if (funcionarioSalvo == null) {
            funcionarioSalvo = funcionarioService.cadastrar(f);
        }
        return funcionarioSalvo;
    }

    private void atualizarHorario(Agendamento destino, Agendamento origem) {
        if(destino.getInicioAgendamento() != null && destino.getFimAgendamento() != null) {
            destino.setInicioAgendamento(origem.getInicioAgendamento());
            destino.setFimAgendamento(origem.getFimAgendamento());
        }
    }

    @Transactional
    public Agendamento removerFuncionario(Integer agendamentoId, Integer funcionarioId) {
        Agendamento agendamento = buscarPorId(agendamentoId);

        if (agendamento.getTipoAgendamento() == TipoAgendamento.SERVICO) {
            int totalFuncionarios = repository.countFuncionariosByAgendamentoId(agendamentoId);

            if (totalFuncionarios <= 1) {
                logService.warning(String.format(
                        "Tentativa de remoção bloqueada: Funcionário ID %d é o único alocado no Agendamento ID %d (tipo SERVICO).",
                        funcionarioId, agendamentoId));
                throw new RegraNegocioException(
                        "Não é possível remover o único funcionário alocado. Isso resultaria no cancelamento do serviço.");
            }
        }

        boolean removido = agendamento.getFuncionarios()
                .removeIf(f -> f.getId().equals(funcionarioId));

        if (!removido) {
            throw new RegraNegocioException(
                    String.format("Funcionário ID %d não está alocado no Agendamento ID %d.", funcionarioId, agendamentoId));
        }

        Agendamento atualizado = repository.save(agendamento);

        logService.info(String.format(
                "Funcionário ID %d removido do Agendamento ID %d com sucesso.",
                funcionarioId, agendamentoId));

        if (atualizado.getFuncionarios().isEmpty()) {
            cancelarAgendamentoSemFuncionario(atualizado);
        }

        return atualizado;
    }

    @Transactional
    public Agendamento adicionarFuncionario(Integer agendamentoId, Integer funcionarioId) {
        Agendamento agendamento = buscarPorId(agendamentoId);
        Funcionario funcionario = funcionarioService.buscarPorId(funcionarioId);

        if (funcionario.getAtivo() == null || !funcionario.getAtivo()) {
            throw new RegraNegocioException("Funcionário inativo não pode ser alocado a um agendamento.");
        }

        boolean conflito = funcionarioService.temConflito(
                funcionarioId,
                agendamento.getDataAgendamento(),
                agendamento.getInicioAgendamento(),
                agendamento.getFimAgendamento()
        );

        if (conflito) {
            logService.warning(String.format(
                    "Conflito de agenda detectado ao tentar alocar Funcionário ID %d ao Agendamento ID %d.",
                    funcionarioId, agendamentoId));
            throw new RegraNegocioException(
                    String.format("Funcionário '%s' possui conflito de horário nesta data e horário.", funcionario.getNome()));
        }

        boolean jaAlocado = agendamento.getFuncionarios().stream()
                .anyMatch(f -> f.getId().equals(funcionarioId));

        if (jaAlocado) {
            throw new RegraNegocioException(
                    String.format("Funcionário '%s' já está alocado neste agendamento.", funcionario.getNome()));
        }

        agendamento.getFuncionarios().add(funcionario);
        Agendamento atualizado = repository.save(agendamento);

        logService.success(String.format(
                "Funcionário ID %d (%s) alocado ao Agendamento ID %d com sucesso.",
                funcionarioId, funcionario.getNome(), agendamentoId));

        return atualizado;
    }

    private void cancelarAgendamentoSemFuncionario(Agendamento agendamento) {
        Status statusCancelado = statusService.buscarOuCriarPorTipoENome("AGENDAMENTO", "CANCELADO");
        agendamento.setStatusAgendamento(statusCancelado);
        repository.save(agendamento);

        if (agendamento.getServico() != null) {
            try {
                Etapa etapaReagendar = etapaService.buscarPorTipoAndEtapa("PEDIDO", "REAGENDAR");
                agendamento.getServico().setEtapa(etapaReagendar);
                servicoService.editar(agendamento.getServico(), agendamento.getServico().getId());
            } catch (Exception e) {
                log.warn("Etapa REAGENDAR não encontrada, mantendo etapa atual do serviço.");
            }
        }

        logService.warning(String.format(
                "Agendamento ID %d cancelado automaticamente por ficar sem funcionário alocado.",
                agendamento.getId()));
    }

    public void validarConflitoAoEditar(Integer agendamentoId, LocalDate data, LocalTime inicio, LocalTime fim) {
        Agendamento agendamento = buscarPorId(agendamentoId);

        for (Funcionario f : agendamento.getFuncionarios()) {
            List<Agendamento> conflitos = repository.findConflitos(f.getId(), data, inicio, fim);
            conflitos = conflitos.stream()
                    .filter(a -> !a.getId().equals(agendamentoId))
                    .toList();

            if (!conflitos.isEmpty()) {
                logService.warning(String.format(
                        "Conflito ao editar Agendamento ID %d: Funcionário '%s' (ID %d) possui conflito no horário.",
                        agendamentoId, f.getNome(), f.getId()));
                throw new RegraNegocioException(
                        String.format("Funcionário '%s' possui conflito de horário no novo horário solicitado.", f.getNome()));
            }
        }
    }
}
