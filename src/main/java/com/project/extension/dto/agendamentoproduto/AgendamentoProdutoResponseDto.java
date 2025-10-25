package com.project.extension.dto.agendamentoproduto;

import com.project.extension.dto.produto.ProdutoResponseDto;

public record AgendamentoProdutoResponseDto(
        Integer quantidadeUtilizada,
        Integer quantidadeReservada,
        ProdutoResponseDto produto
) {
}
