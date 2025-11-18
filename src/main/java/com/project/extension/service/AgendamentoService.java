package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.strategy.agendamento.AgendamentoContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final LogService logService;

    public Agendamento salvar(Agendamento agendamento) {
        Agendamento agendamentoProcessado = agendamentoContext.processarAgendamento(agendamento);
        Agendamento agendamentoSalvo = repository.save(agendamentoProcessado);
        // Log de Auditoria: Registro de criação no BD
        String mensagem = String.format("Novo Agendamento ID %d criado com sucesso. Tipo: %s, Data: %s.",
                agendamentoSalvo.getId(),
                agendamentoSalvo.getTipoAgendamento(),
                agendamentoSalvo.getDataAgendamento());
        logService.success(mensagem); // Usando SUCCESS para indicar uma ação de criação bem-sucedida
        return agendamentoSalvo;
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        log.debug("Iniciando edição do Agendamento ID {}.", id);
        Agendamento destino = buscarPorId(id);

        atualizarDadosBasicos(destino, origem);
        atualizarEndereco(destino, origem);
        atualizarStatus(destino, origem);
        atualizarFuncionarios(destino, origem);

        Agendamento atualizado = repository.save(destino);
        // Log de Auditoria: Registro de atualização no BD
        String mensagem = String.format("Agendamento ID %d atualizado com sucesso. Novo Status: %s, Data: %s.",
                atualizado.getId(),
                atualizado.getStatusAgendamento() != null ? atualizado.getStatusAgendamento().getNome() : "N/A",
                atualizado.getDataAgendamento());
        logService.info(mensagem); // Usando INFO para indicar uma atualização
        return atualizado;
    }

    public void deletar(Integer id) {
        Agendamento agendamento = buscarPorId(id);

        logService.warning(String.format("Tentativa de exclusão lógica/desvinculação do Agendamento ID %d.", id));

        agendamento.getFuncionarios().clear();
        repository.save(agendamento);

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

    private void atualizarDadosBasicos(Agendamento destino, Agendamento origem) {
        destino.setTipoAgendamento(origem.getTipoAgendamento());
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
                    .collect(Collectors.toList());

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
}
