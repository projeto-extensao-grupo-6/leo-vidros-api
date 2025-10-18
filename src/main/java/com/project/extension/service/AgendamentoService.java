package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final EnderecoService enderecoService;
    private final FuncionarioService funcionarioService;
    private final StatusService statusService;
    private final PedidoService pedidoService;

    public Agendamento salvar(Agendamento agendamento) {
        // Endereço
        Endereco enderecoSalvo = enderecoService.buscarPorCep(agendamento.getEndereco().getCep());
        if (enderecoSalvo == null) {
            enderecoSalvo = enderecoService.cadastrar(agendamento.getEndereco());
        }
        agendamento.setEndereco(enderecoSalvo);

        // Pedido
        agendamento.setPedido(pedidoService.buscarPorId(agendamento.getPedido().getId()));

        // Status
        Status statusSalvo = statusService.buscarPorTipoAndStatus(
                agendamento.getStatusAgendamento().getTipo(),
                agendamento.getStatusAgendamento().getNome()
        );
        if (statusSalvo == null) {
            statusSalvo = statusService.cadastrar(agendamento.getStatusAgendamento());
        }
        agendamento.setStatusAgendamento(statusSalvo);

        // Funcionários
        List<Funcionario> funcionariosSalvos = new ArrayList<>();
        for (Funcionario f : agendamento.getFuncionarios()) {
            Funcionario funcionarioSalvo;
            if (f.getId() != null) {
                funcionarioSalvo = funcionarioService.buscarPorId(f.getId());
            } else {
                funcionarioSalvo = funcionarioService.buscarPorTelefone(f.getTelefone());
                if (funcionarioSalvo == null) {
                    funcionarioSalvo = funcionarioService.cadastrar(f);
                }
            }
            funcionariosSalvos.add(funcionarioSalvo);
        }

        agendamento.getFuncionarios().clear();
        agendamento.getFuncionarios().addAll(funcionariosSalvos);

        Agendamento agendamentoSalvo = repository.save(agendamento);
        log.info("Agendamento salvo com sucesso! ID: {}", agendamentoSalvo.getId());
        return agendamentoSalvo;
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        Agendamento destino = buscarPorId(id);

        // Campos simples
        destino.setTipoAgendamento(origem.getTipoAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setObservacao(origem.getObservacao());

        // Endereço
        if (origem.getEndereco() != null) {
            Endereco enderecoAtualizado = enderecoService.editar(origem.getEndereco(), origem.getEndereco().getId());
            destino.setEndereco(enderecoAtualizado);
        }

        // Status
        if (origem.getStatusAgendamento() != null) {
            Status statusAtualizado = statusService.buscarOuCriarPorTipoENome(
                    origem.getStatusAgendamento().getTipo(),
                    origem.getStatusAgendamento().getNome()
            );
            destino.setStatusAgendamento(statusAtualizado);
        }

        // Funcionários
        if (origem.getFuncionarios() != null) {
            List<Funcionario> funcionariosValidados = new ArrayList<>();
            for (Funcionario f : origem.getFuncionarios()) {
                Funcionario funcionarioSalvo;
                if (f.getId() != null) {
                    funcionarioSalvo = funcionarioService.buscarPorId(f.getId());
                } else {
                    funcionarioSalvo = funcionarioService.buscarPorTelefone(f.getTelefone());
                    if (funcionarioSalvo == null) {
                        funcionarioSalvo = funcionarioService.cadastrar(f);
                    }
                }
                funcionariosValidados.add(funcionarioSalvo);
            }
            destino.getFuncionarios().clear();
            destino.getFuncionarios().addAll(funcionariosValidados);
        }

        Agendamento atualizado = repository.save(destino);
        log.info("Agendamento atualizado com sucesso! ID: {}", atualizado.getId());
        return atualizado;
    }

    public void deletar(Integer id) {
        Agendamento agendamento = buscarPorId(id);

        // Apenas desvincula funcionários, sem substituir lista imutável
        agendamento.getFuncionarios().clear();
        repository.save(agendamento);

        log.info("Agendamento ID {} desvinculado de funcionários e mantido no histórico.", id);
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
}
