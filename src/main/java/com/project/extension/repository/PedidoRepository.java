package com.project.extension.repository;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Page<Pedido> findAllByServico_Etapa(Etapa etapa, Pageable pageable);

    Page<Pedido> findByTipoPedidoIgnoreCase(String tipo, Pageable pageable);

    Page<Pedido> findByServicoIsNotNull(Pageable pageable);

    Page<Pedido> findByItensPedidoIsNotEmpty(Pageable pageable);

    @Query(value = """
        SELECT COALESCE(SUM(valor_total), 0)
        FROM pedido
        WHERE MONTH(created_at) = MONTH(CURRENT_DATE)
          AND YEAR(created_at) = YEAR(CURRENT_DATE)
          AND ativo = true
    """, nativeQuery = true)
    BigDecimal sumFaturamentoMesAtual();

    @Query(value = """
        SELECT COALESCE(SUM(valor_total), 0)
        FROM pedido
        WHERE MONTH(created_at) = MONTH(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
          AND YEAR(created_at) = YEAR(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
          AND ativo = true
    """, nativeQuery = true)
    BigDecimal sumFaturamentoMesAnterior();

    @Query(value = """
        SELECT MONTH(created_at) as mes, COALESCE(SUM(valor_total), 0) as valor
        FROM pedido
        WHERE YEAR(created_at) = YEAR(CURRENT_DATE)
          AND ativo = true
        GROUP BY MONTH(created_at)
        ORDER BY mes
    """, nativeQuery = true)
    List<Object[]> sumFaturamentoPorMesAnoAtual();
}
