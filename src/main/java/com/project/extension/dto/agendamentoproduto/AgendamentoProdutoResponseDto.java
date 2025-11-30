package com.project.extension.dto.agendamentoproduto;

import com.project.extension.dto.produto.ProdutoResponseDto;

import java.math.BigDecimal;

public record AgendamentoProdutoResponseDto(
        BigDecimal quantidadeUtilizada,
        BigDecimal quantidadeReservada,
        ProdutoResponseDto produto
) {
}
