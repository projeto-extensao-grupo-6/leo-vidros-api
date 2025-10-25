package com.project.extension.service;

import com.project.extension.entity.AgendamentoProduto;
import com.project.extension.exception.naoencontrado.AgendamentoProdutoNaoEncontradoException;
import com.project.extension.repository.AgendamentoProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AgendamentoProdutoService {

    private final AgendamentoProdutoRepository repository;

    public AgendamentoProduto cadastrar(AgendamentoProduto agendamentoProduto) {
        log.info("Cadastrando vínculo entre agendamento {} e produto {}",
                agendamentoProduto.getAgendamento().getId(),
                agendamentoProduto.getProduto().getId());

        return repository.save(agendamentoProduto);
    }

    public AgendamentoProduto buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("AgendamentoProduto com ID {} não encontrado", id);
            return new AgendamentoProdutoNaoEncontradoException();
        });
    }

    public AgendamentoProduto editar(AgendamentoProduto atualizacao, Integer id) {
        AgendamentoProduto existente = this.buscarPorId(id);

        if (atualizacao.getQuantidadeReservada() != null) {
            existente.setQuantidadeReservada(atualizacao.getQuantidadeReservada());
        }

        if (atualizacao.getQuantidadeUtilizada() != null) {
            existente.setQuantidadeUtilizada(atualizacao.getQuantidadeUtilizada());
        }

        log.info("Atualizando vínculo de agendamento {} com produto {}: reservada={}, utilizada={}",
                existente.getAgendamento().getId(),
                existente.getProduto().getId(),
                existente.getQuantidadeReservada(),
                existente.getQuantidadeUtilizada());

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        AgendamentoProduto existente = this.buscarPorId(id);
        log.info("Deletando vínculo de agendamento {} e produto {}",
                existente.getAgendamento().getId(),
                existente.getProduto().getId());
        repository.delete(existente);
    }
}
