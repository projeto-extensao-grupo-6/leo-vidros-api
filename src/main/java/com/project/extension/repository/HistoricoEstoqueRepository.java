package com.project.extension.repository;

import com.project.extension.entity.HistoricoEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoEstoqueRepository extends JpaRepository<HistoricoEstoque, Integer> {

    Page<HistoricoEstoque> findByEstoqueId(Integer estoqueId, Pageable pageable);

    void deleteByPedidoId(Integer id);
}
