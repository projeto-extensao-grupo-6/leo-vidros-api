package com.project.extension.repository;

import com.project.extension.entity.HistoricoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoEstoqueRepository extends JpaRepository<HistoricoEstoque, Integer> {

    List<HistoricoEstoque> findByEstoqueId(Integer estoqueId);
}
