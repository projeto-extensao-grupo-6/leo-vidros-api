package com.project.extension.repository;

import com.project.extension.entity.AgendamentoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AgendamentoProdutoRepository extends JpaRepository<AgendamentoProduto, Integer> {

    @Query(
            """
            SELECT COALESCE(SUM(ap.quantidadeReservada), 0)
            FROM AgendamentoProduto ap
            JOIN ap.agendamento a
            JOIN a.statusAgendamento s
            WHERE ap.produto.id = :produtoId
              AND s.nome NOT IN ('CANCELADO', 'CONCLUIDO', 'CONCLUÍDO', 'INATIVO')
            """
    )
    BigDecimal somarReservasAtivasPorProdutoId(Integer produtoId);
}
