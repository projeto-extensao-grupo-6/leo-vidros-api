package com.project.extension.dto.estoque;

import com.project.extension.dto.produto.ProdutoResponseDto;

public record EstoqueResponseDto(
        Integer id,
        Integer quantidadeTotal,
        Integer quantidadeDisponivel,
        Integer reservado,
        String localizacao,
        ProdutoResponseDto produto
) {
}
