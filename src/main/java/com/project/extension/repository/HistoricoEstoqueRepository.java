package com.project.extension.repository;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.HistoricoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoEstoqueRepository extends JpaRepository<HistoricoEstoque, Long> {
    void deleteByEstoque(Estoque estoque);
}
