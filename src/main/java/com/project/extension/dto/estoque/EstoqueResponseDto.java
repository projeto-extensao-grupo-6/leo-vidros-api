package com.project.extension.dto.estoque;

import com.project.extension.dto.produto.ProdutoResponseDto;

public record EstoqueResponseDto(
        Integer id,
        Integer quantidade,
        Integer reservado,
        String localizacao,
        ProdutoResponseDto produto
) {
}
