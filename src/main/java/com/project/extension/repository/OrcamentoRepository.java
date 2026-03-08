package com.project.extension.repository;

import com.project.extension.entity.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {

    List<Orcamento> findByAtivoTrueOrderByCreatedAtDesc();

    List<Orcamento> findByPedidoIdAndAtivoTrue(Integer pedidoId);

    List<Orcamento> findByClienteIdAndAtivoTrue(Integer clienteId);

    Optional<Orcamento> findByNumeroOrcamento(String numeroOrcamento);
}
