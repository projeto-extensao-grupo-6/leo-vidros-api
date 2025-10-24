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

    public HistoricoEstoque cadastrar(HistoricoEstoque historicoEstoque){
        if (historicoEstoque.getUsuario() == null || historicoEstoque.getEstoque() == null) {
            throw new IllegalArgumentException("Usuário e Estoque são obrigatórios para registrar histórico.");
        }

        return repository.save(historicoEstoque);
    }

    public List<HistoricoEstoque> listar() {
        List<HistoricoEstoque> historicoEstoques = repository.findAll();
        log.info("Total de registros de histórico estoque encontrados: {}", historicoEstoques.size());
        return historicoEstoques;
    }

    public HistoricoEstoque buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Histórico Estoque com ID {} não encontrado", id);
                    return new HistoricoEstoqueNaoEncontradoException();
                });
    }
}
