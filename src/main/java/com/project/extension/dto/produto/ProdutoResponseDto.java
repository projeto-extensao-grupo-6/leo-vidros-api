package com.project.extension.dto.produto;

import com.project.extension.dto.atributo.AtributoProdutoResponseDto;

import java.util.List;

public record ProdutoResponseDto(

        Integer id,
        String nome,
        String descricao,
        String unidademedida,
        Double preco,
        Boolean ativo,
        List<AtributoProdutoResponseDto> atributos
) {
}
