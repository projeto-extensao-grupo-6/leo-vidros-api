package com.project.extension.controller.dashboard.dto;

import java.math.BigDecimal;

public record EstoqueCriticoResponseDto(
        Integer id,
        BigDecimal quantidadeTotal,
        BigDecimal quantidadeDisponivel,
        BigDecimal reservado,
        String localizacao,
        String nomeProduto,
        String descricaoProduto,
        String unidadeMedida,
        BigDecimal preco,
        Integer nivelMinimo,
        Integer nivelMaximo
) {}