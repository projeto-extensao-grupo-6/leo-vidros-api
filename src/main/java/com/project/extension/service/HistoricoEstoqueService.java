package com.project.extension.service;

import com.project.extension.entity.HistoricoEstoque;
import com.project.extension.exception.naoencontrado.HistoricoEstoqueNaoEncontradoException;
import com.project.extension.repository.HistoricoEstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class HistoricoEstoqueService {
    private final HistoricoEstoqueRepository repository;
    private final LogService logService;

    public HistoricoEstoque cadastrar(HistoricoEstoque historicoEstoque){
        if (historicoEstoque.getUsuario() == null || historicoEstoque.getEstoque() == null) {
            logService.error("Tentativa de cadastrar HistóricoEstoque sem Usuário ou Estoque associado.");
            throw new IllegalArgumentException("Usuário e Estoque são obrigatórios para registrar histórico.");
        }
        HistoricoEstoque salvo = repository.save(historicoEstoque);
        String mensagem = String.format("Novo registro de HistóricoEstoque ID %d criado com sucesso. Tipo: %s, Qtd: %d. (Auditado).",
                salvo.getId(),
                salvo.getTipoMovimentacao(),
                salvo.getQuantidade());
        logService.info(mensagem);
        log.info("Histórico de Estoque ID {} salvo com sucesso.", salvo.getId());

        return salvo;
    }

    public List<HistoricoEstoque> listar() {
        List<HistoricoEstoque> historicoEstoques = repository.findAll();
        logService.info(String.format("Busca por todos os registros de Histórico Estoque realizada. Total: %d.", historicoEstoques.size()));
        return historicoEstoques;
    }

    public List<HistoricoEstoque> buscarPorEstoqueId(Integer estoqueId) {
        List<HistoricoEstoque> historicos = repository.findByEstoqueId(estoqueId);

        if (historicos.isEmpty()) {
            logService.error(String.format("Nenhum histórico encontrado para o estoque ID %d.", estoqueId));
            log.error("Nenhum histórico encontrado para o estoque ID {}", estoqueId);
            throw new HistoricoEstoqueNaoEncontradoException();
        }

        return historicos;
    }

}
