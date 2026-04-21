package com.project.extension.repository;

import com.project.extension.entity.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {

    Page<Orcamento> findByAtivoTrueOrderByCreatedAtDesc(Pageable pageable);

    Page<Orcamento> findByPedidoIdAndAtivoTrue(Integer pedidoId, Pageable pageable);

    List<Orcamento> findByClienteIdAndAtivoTrue(Integer clienteId);

    Optional<Orcamento> findByNumeroOrcamento(String numeroOrcamento);

    long countByPedidoIdAndAtivoTrue(Integer pedidoId);

    void deleteByPedidoId(Integer pedidoId);

    @Query(value = """
        SELECT COUNT(*)
        FROM orcamento o
        JOIN status s ON o.status_id = s.id
        WHERE s.nome IN ('RASCUNHO', 'ENVIADO', 'EM ANALISE')
          AND o.ativo = true
    """, nativeQuery = true)
    int countOrcamentosAbertos();

    @Query(value = """
        SELECT COALESCE(SUM(o.valor_total), 0)
        FROM orcamento o
        JOIN status s ON o.status_id = s.id
        WHERE s.nome IN ('RASCUNHO', 'ENVIADO', 'EM ANALISE')
          AND o.ativo = true
    """, nativeQuery = true)
    BigDecimal sumValorOrcamentosAbertos();
}
