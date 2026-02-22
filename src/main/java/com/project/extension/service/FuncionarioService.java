package com.project.extension.service;

import com.project.extension.dto.funcionario.AgendaFuncionarioResponseDto;
import com.project.extension.dto.funcionario.FuncionarioDisponivelResponseDto;
import com.project.extension.entity.Agendamento;
import com.project.extension.entity.Funcionario;
import com.project.extension.exception.naoencontrado.FuncionarioNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.repository.FuncionarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final LogService logService;

    public Funcionario cadastrar(Funcionario funcionario) {
        Funcionario funcionarioSalvo = repository.save(funcionario);
        String mensagem = String.format("Novo Funcionário ID %d cadastrado com sucesso. Nome: %s, Função: %s.",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getFuncao());
        logService.success(mensagem);
        return funcionarioSalvo;
    }

    public Funcionario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Funcionário com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.warn("Funcionário com ID {} não encontrado", id);
            return new FuncionarioNaoEncontradoException();
        });
    }

    public Funcionario buscarPorTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }

    public List<Funcionario> listar() {
        List<Funcionario> funcionarios = repository.findAll();
        logService.info(String.format("Busca por todos os funcionários realizada. Total de registros: %d.", funcionarios.size()));
        return funcionarios;
    }

    private void atualizarCampos(Funcionario destino, Funcionario origem) {
        destino.setNome(origem.getNome());
        destino.setTelefone(origem.getTelefone());
        destino.setFuncao(origem.getFuncao());
        destino.setContrato(origem.getContrato());
        destino.setEscala(origem.getEscala());
        destino.setAtivo(origem.getAtivo());
        destino.setAtivo(origem.getAtivo());
        log.trace("Campos do funcionário atualizados em memória.");
    }

    public Funcionario editar(Funcionario origem, Integer id) {
        Funcionario destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Funcionario funcionarioAtualizado = repository.save(destino);
        String mensagem = String.format("Funcionário ID %d atualizado com sucesso. Nome: %s, Função: %s.",
                funcionarioAtualizado.getId(), funcionarioAtualizado.getNome(), funcionarioAtualizado.getFuncao());
        logService.info(mensagem);
        return funcionarioAtualizado;
    }

    public void deletar(Integer id) {
        Funcionario funcionarioParaDeletar = this.buscarPorId(id);
        repository.deleteById(id);
        String mensagem = String.format("Funcionário ID %d (Nome: %s) deletado com sucesso.",
                id, funcionarioParaDeletar.getNome());
        logService.info(mensagem);
    }

    public List<AgendaFuncionarioResponseDto> buscarAgenda(Integer funcionarioId, LocalDate dataInicio, LocalDate dataFim) {
        buscarPorId(funcionarioId);

        List<Agendamento> agendamentos = agendamentoRepository
                .findAgendamentosByFuncionarioAndPeriodo(funcionarioId, dataInicio, dataFim);

        logService.info(String.format("Consulta de agenda do Funcionário ID %d entre %s e %s. Total: %d agendamentos.",
                funcionarioId, dataInicio, dataFim, agendamentos.size()));

        return agendamentos.stream()
                .map(this::toAgendaResponse)
                .toList();
    }

    private AgendaFuncionarioResponseDto toAgendaResponse(Agendamento a) {
        String clienteNome = null;
        String etapaServico = null;
        String servicoNome = null;
        String servicoCodigo = null;

        if (a.getServico() != null) {
            servicoNome = a.getServico().getNome();
            servicoCodigo = a.getServico().getCodigo();

            if (a.getServico().getEtapa() != null) {
                etapaServico = a.getServico().getEtapa().getNome();
            }

            if (a.getServico().getPedido() != null && a.getServico().getPedido().getCliente() != null) {
                clienteNome = a.getServico().getPedido().getCliente().getNome();
            }
        }

        return new AgendaFuncionarioResponseDto(
                a.getId(),
                a.getDataAgendamento(),
                a.getInicioAgendamento(),
                a.getFimAgendamento(),
                a.getTipoAgendamento() != null ? a.getTipoAgendamento().name() : null,
                a.getStatusAgendamento() != null ? a.getStatusAgendamento().getNome() : null,
                clienteNome,
                servicoNome,
                servicoCodigo,
                etapaServico
        );
    }

    public List<FuncionarioDisponivelResponseDto> buscarDisponiveis(LocalDate data, LocalTime inicio, LocalTime fim) {
        List<Funcionario> disponiveis = repository.findDisponiveis(data, inicio, fim);

        logService.info(String.format("Consulta de funcionários disponíveis em %s das %s às %s. Total: %d disponíveis.",
                data, inicio, fim, disponiveis.size()));

        return disponiveis.stream()
                .map(f -> new FuncionarioDisponivelResponseDto(
                        f.getId(),
                        f.getNome(),
                        f.getTelefone(),
                        f.getFuncao(),
                        f.getEscala(),
                        f.getAtivo()
                ))
                .toList();
    }

    public boolean temConflito(Integer funcionarioId, LocalDate data, LocalTime inicio, LocalTime fim) {
        List<Agendamento> conflitos = agendamentoRepository.findConflitos(funcionarioId, data, inicio, fim);
        if (!conflitos.isEmpty()) {
            logService.warning(String.format(
                    "Conflito de agenda detectado para Funcionário ID %d em %s das %s às %s. Conflitos: %d.",
                    funcionarioId, data, inicio, fim, conflitos.size()));
        }
        return !conflitos.isEmpty();
    }
}
