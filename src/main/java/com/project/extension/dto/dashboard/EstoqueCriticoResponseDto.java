package com.project.extension.dto.dashboard;

public record EstoqueCriticoResponseDto(
        Integer quantidadeTotal,
        Integer quantidadeDisponivel,
        Integer reservado,
        String localizacao,
        String nomeProduto,
        String descricaoProduto,
        String unidadeMedida,
        Double preco,
        Integer nivelMinimo,
        Integer nivelMaximo
) {}