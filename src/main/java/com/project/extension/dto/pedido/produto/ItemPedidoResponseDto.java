package com.project.extension.dto.pedido.produto;

import java.math.BigDecimal;
public record ItemPedidoResponseDto(
        Integer id,
        Integer estoqueId,
        String nomeProduto,
        BigDecimal quantidadeSolicitada,
        BigDecimal precoUnitarioNegociado,
        BigDecimal subtotal,
        String observacao
) {
}
