package com.project.extension.repository;

import com.project.extension.entity.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {

    Page<Orcamento> findByAtivoTrueOrderByCreatedAtDesc(Pageable pageable);

    Page<Orcamento> findByPedidoIdAndAtivoTrue(Integer pedidoId, Pageable pageable);

    List<Orcamento> findByClienteIdAndAtivoTrue(Integer clienteId);

    Optional<Orcamento> findByNumeroOrcamento(String numeroOrcamento);

    void deleteByPedidoId(Integer pedidoId);
}
