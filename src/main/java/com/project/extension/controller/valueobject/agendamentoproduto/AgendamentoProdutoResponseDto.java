package com.project.extension.controller.valueobject.agendamentoproduto;

import com.project.extension.controller.pedido.produto.dto.ProdutoResponseDto;

import java.math.BigDecimal;

public record AgendamentoProdutoResponseDto(
        BigDecimal quantidadeUtilizada,
        BigDecimal quantidadeReservada,
        ProdutoResponseDto produto
) {
}
