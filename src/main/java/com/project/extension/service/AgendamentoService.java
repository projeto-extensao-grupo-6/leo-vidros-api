package com.project.extension.service;

import com.project.extension.entity.Agendamento;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Funcionario;
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

    public Agendamento salvar(Agendamento agendamento) {
        Endereco enderecoExistente = enderecoService.buscarPorCep(agendamento.getEndereco().getCep());
        Endereco enderecoSalvo = enderecoExistente != null
                ? enderecoExistente
                : enderecoService.cadastrar(agendamento.getEndereco());

        agendamento.setEndereco(enderecoSalvo);

        List<Funcionario> funcionariosSalvos = new ArrayList<>();

        for (Funcionario funcionario : agendamento.getFuncionarios()) {
            Funcionario funcionarioSalvo;

            if (funcionario.getId() != null) {
                funcionarioSalvo = funcionarioService.buscarPorId(funcionario.getId());
            } else {
                Funcionario existente = funcionarioService.buscarPorTelefone(funcionario.getTelefone());
                funcionarioSalvo = existente != null
                        ? existente
                        : funcionarioService.cadastrar(funcionario);
            }
            funcionariosSalvos.add(funcionarioSalvo);
        }

        agendamento.setFuncionarios(funcionariosSalvos);

        Agendamento agendamentoSalvo = repository.save(agendamento);
        log.info("Agendamento salvo com sucesso! ID: {}", agendamentoSalvo.getId());

        return agendamentoSalvo;
    }


    public List<Agendamento> buscarTodos(){
        List<Agendamento> lista = repository.findAll();
        log.info("Total de Agendamentos encontrados: " + lista.size());
        return lista;
    }

    public Agendamento buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Agendamento com ID " + id + " não encontrado");
            return new AgendamentoNaoEncontradoException();
        });
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        Agendamento destino = buscarPorId(id);

        destino.setTipoAgendamento(origem.getTipoAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setStatusAgendamento(origem.getStatusAgendamento());
        destino.setObservacao(origem.getObservacao());

        if (origem.getEndereco() != null) {
            Endereco enderecoAtualizado = enderecoService.editar(origem.getEndereco(), origem.getEndereco().getId());
            destino.setEndereco(enderecoAtualizado);
        }

        if (origem.getFuncionarios() != null) {
            List<Funcionario> funcionariosValidados = origem.getFuncionarios().stream()
                    .map(f -> f.getId() != null
                            ? funcionarioService.buscarPorId(f.getId())
                            : funcionarioService.cadastrar(f))
                    .toList();
            destino.setFuncionarios(funcionariosValidados);
        }

        Agendamento atualizado = repository.save(destino);
        log.info("Agendamento atualizado com sucesso! ID: {}", atualizado.getId());
        return atualizado;
    }

    public void deletar(Integer id) {
        Agendamento agendamento = buscarPorId(id);

        agendamento.getFuncionarios().clear();
        repository.save(agendamento);

        log.info("Agendamento ID {} desvinculado de funcionários e mantido no histórico.", id);
    }
}
