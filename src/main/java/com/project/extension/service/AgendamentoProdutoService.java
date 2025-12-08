package com.project.extension.service;

import com.project.extension.entity.AgendamentoProduto;
import com.project.extension.exception.naoencontrado.AgendamentoProdutoNaoEncontradoException;
import com.project.extension.repository.AgendamentoProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j // Mantido para logs técnicos/debug, mas não usado para logs de INFO/ERROR de negócio
@AllArgsConstructor
public class AgendamentoProdutoService {
    private final AgendamentoProdutoRepository repository;
    private final LogService logService;

    public AgendamentoProduto cadastrar(AgendamentoProduto agendamentoProduto) {
        log.debug("Iniciando persistência do vínculo AgendamentoProduto.");
        AgendamentoProduto salvo = repository.save(agendamentoProduto);
        String mensagem = String.format("Vínculo AgendamentoProduto ID %d criado. Agendamento ID: %d, Produto ID: %d.",
                salvo.getId(),
                salvo.getAgendamento().getId(),
                salvo.getProduto().getId());
        logService.success(mensagem);

        return salvo;
    }

    public AgendamentoProduto buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("AgendamentoProduto com ID %d não encontrado durante busca.", id);
            logService.error(mensagem);
            log.warn(mensagem);

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

        AgendamentoProduto atualizado = repository.save(existente);

        // Log de Auditoria (LogService) - Ação de negócio concluída
        String mensagem = String.format("Vínculo AgendamentoProduto ID %d atualizado. Reservado: %f, Utilizado: %f.",
                id,
                atualizado.getQuantidadeReservada(),
                atualizado.getQuantidadeUtilizada());
        logService.info(mensagem);

        return atualizado;
    }

    public void deletar(Integer id) {
        AgendamentoProduto existente = this.buscarPorId(id);
        repository.delete(existente);

        // Log de Auditoria (LogService) - Ação de negócio concluída
        String mensagem = String.format("Vínculo AgendamentoProduto ID %d deletado. Agendamento ID: %d, Produto ID: %d.",
                id,
                existente.getAgendamento().getId(),
                existente.getProduto().getId());
        logService.info(mensagem);
    }
}