package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.strategy.agendamento.AgendamentoContext;
import jakarta.persistence.EntityNotFoundException;
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
    private final PedidoService pedidoService;

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getPedido() != null && agendamento.getPedido().getId() != null) {
            Pedido pedidoCompleto = pedidoService.buscarPorId(agendamento.getPedido().getId());
            agendamento.setPedido(pedidoCompleto);
        }

        Agendamento agendamentoProcessado = agendamentoContext.processarAgendamento(agendamento);
        Agendamento agendamentoSalvo = repository.save(agendamentoProcessado);
        log.info("Agendamento salvo com sucesso! ID: {}", agendamentoSalvo.getId());
        return agendamentoSalvo;
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        Agendamento destino = buscarPorId(id);

        atualizarDadosBasicos(destino, origem);
        atualizarEndereco(destino, origem);
        atualizarHorario(destino, origem);
        atualizarStatus(destino, origem);
        atualizarFuncionarios(destino, origem);

        Agendamento atualizado = repository.save(destino);
        log.info("Agendamento atualizado com sucesso! ID: {}", atualizado.getId());
        return atualizado;
    }

    public void deletar(Integer id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setPedido(null);
        repository.delete(agendamento);

        log.info("Agendamento ID {} deletado com sucesso", id);
    }

    public Agendamento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Agendamento com ID {} não encontrado", id);
                    return new AgendamentoNaoEncontradoException();
                });
    }

    public List<Agendamento> buscarTodos() {
        List<Agendamento> lista = repository.findAll();
        log.info("Total de Agendamentos encontrados: {}", lista.size());
        return lista;
    }

    private void atualizarDadosBasicos(Agendamento destino, Agendamento origem) {
        destino.setTipoAgendamento(origem.getTipoAgendamento());
        destino.setInicioAgendamento(origem.getInicioAgendamento());
        destino.setFimAgendamento(origem.getFimAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setObservacao(origem.getObservacao());
    }

    private void atualizarEndereco(Agendamento destino, Agendamento origem) {
        if (origem.getEndereco() != null) {
            Endereco enderecoAtualizado = enderecoService.editar(origem.getEndereco(), origem.getEndereco().getId());
            destino.setEndereco(enderecoAtualizado);
        }
    }

    private void atualizarStatus(Agendamento destino, Agendamento origem) {
        if (origem.getStatusAgendamento() != null) {
            Status statusAtualizado = statusService.buscarOuCriarPorTipoENome(
                    origem.getStatusAgendamento().getTipo(),
                    origem.getStatusAgendamento().getNome()
            );
            destino.setStatusAgendamento(statusAtualizado);
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

    private void atualizarHorario(Agendamento destino, Agendamento origem) {
        if(destino.getInicioAgendamento() != null && destino.getFimAgendamento() != null) {
            destino.setInicioAgendamento(origem.getInicioAgendamento());
            destino.setFimAgendamento(origem.getFimAgendamento());
        }
    }
}
