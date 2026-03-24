package com.project.extension.controller.estoque.dto;

import com.project.extension.controller.pedido.produto.dto.ProdutoResponseDto;

import java.math.BigDecimal;

public record EstoqueResponseDto(
        Integer id,
        BigDecimal quantidadeTotal,
        BigDecimal quantidadeDisponivel,
        BigDecimal reservado,
        String localizacao,
        ProdutoResponseDto produto
) {
}
