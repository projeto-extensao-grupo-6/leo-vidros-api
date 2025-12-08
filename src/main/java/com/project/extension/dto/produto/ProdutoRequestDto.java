package com.project.extension.dto.produto;

import com.project.extension.dto.atributo.AtributoProdutoRequestDto;
import com.project.extension.dto.metrica.MetricaEstoqueRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ProdutoRequestDto(
             @NotBlank String nome,
             @NotBlank String descricao,
             @NotBlank String unidademedida,
             @Positive Double preco,
             @NotNull Boolean ativo,
             @Valid @NotNull MetricaEstoqueRequestDto metrica,
             @Valid @NotNull List<AtributoProdutoRequestDto> atributos
) {
}
