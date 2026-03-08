package com.project.extension.dto.orcamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrcamentoItemRequestDto(
        Integer produtoId,

        @NotBlank(message = "A descrição do item é obrigatória")
        String descricao,

        @NotNull(message = "A quantidade é obrigatória")
        BigDecimal quantidade,

        @NotNull(message = "O preço unitário é obrigatório")
        BigDecimal precoUnitario,

        BigDecimal desconto,
        String observacao,
        Integer ordem
) {}
