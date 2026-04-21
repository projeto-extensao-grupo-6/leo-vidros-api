package com.project.extension.controller.pedido.servico.dto.produto;

import java.math.BigDecimal;
public record ItemPedidoResponseDto(
        Integer id,
        Integer estoqueId,
        Integer produtoId,
        String nomeProduto,
        BigDecimal quantidadeSolicitada,
        BigDecimal precoUnitarioNegociado,
        BigDecimal subtotal,
        String observacao
) {
}
