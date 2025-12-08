package com.project.extension.dto.produto;

import com.project.extension.dto.atributo.AtributoProdutoResponseDto;
import com.project.extension.dto.metrica.MetricaEstoqueResponseDto;

import java.util.List;

public record ProdutoResponseDto(

        Integer id,
        String nome,
        String descricao,
        String unidademedida,
        Double preco,
        Boolean ativo,
        MetricaEstoqueResponseDto metrica,
        List<AtributoProdutoResponseDto> atributos
) {
}
