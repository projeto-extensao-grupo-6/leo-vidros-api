package com.project.extension.controller.orcamento.dto;

import java.math.BigDecimal;

public record OrcamentoItemResponseDto(
        Integer id,
        Integer produtoId,
        String produtoNome,
        String descricao,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal desconto,
        BigDecimal subtotal,
        String observacao,
        Integer ordem
) {}
