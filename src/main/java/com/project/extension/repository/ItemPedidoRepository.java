package com.project.extension.repository;

import com.project.extension.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    @Query("""
            SELECT COALESCE(SUM(ip.quantidadeSolicitada), 0)
            FROM ItemPedido ip
            JOIN ip.pedido p
            JOIN p.servico s
            LEFT JOIN s.etapa e
            WHERE ip.estoque.produto.id = :produtoId
              AND p.ativo = true
              AND COALESCE(s.ativo, true) = true
              AND (:pedidoIdIgnorado IS NULL OR p.id <> :pedidoIdIgnorado)
              AND (e IS NULL OR e.nome NOT IN ('CONCLUIDO', 'CONCLUÍDO', 'CANCELADO', 'INATIVO'))
              AND NOT EXISTS (
                    SELECT 1
                    FROM Agendamento a
                    JOIN a.statusAgendamento st
                    WHERE a.servico = s
                      AND a.tipoAgendamento = com.project.extension.entity.TipoAgendamento.SERVICO
                      AND st.nome NOT IN ('CANCELADO', 'CONCLUIDO', 'CONCLUÍDO', 'INATIVO')
              )
            """)
    BigDecimal somarReservasDetalheServicoAtivasPorProdutoId(
            @Param("produtoId") Integer produtoId,
            @Param("pedidoIdIgnorado") Integer pedidoIdIgnorado
    );
}
