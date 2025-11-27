package com.project.extension.dto.estoque;

import com.project.extension.dto.produto.ProdutoResponseDto;

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
