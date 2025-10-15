package com.project.extension.service;

import com.project.extension.entity.Agendamento;
import com.project.extension.entity.Endereco;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public Agendamento salvar(Agendamento agendamento) {
        Agendamento agendamentoSalvo = repository.save(agendamento);
        log.info("Agendamento salvo com sucesso!");
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

    private void editarCampos(Agendamento origem, Agendamento destino) {
        destino.setTipoAgendamento(origem.getTipoAgendamento());
        destino.setDataAgendamento(origem.getDataAgendamento());
        destino.setStatusAgendamento(origem.getStatusAgendamento());
        destino.setObservacao(origem.getObservacao());
    }

    public Agendamento editar(Agendamento origem, Integer id) {
        Agendamento destino = this.buscarPorId(id);

        editarCampos(origem, destino);
        Agendamento agendamentoAtualizado = repository.save(destino);
        log.info("Agendamento atualizado com sucesso!");
        return agendamentoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Agendamento deletado com sucesso!");
    }
}
