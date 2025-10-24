package com.project.extension.service;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.HistoricoEstoque;
import com.project.extension.entity.Usuario;
import com.project.extension.repository.HistoricoEstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
