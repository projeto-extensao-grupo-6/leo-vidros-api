package com.project.extension.dto.agendamentoproduto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AgendamentoProdutoRequestDto(
        @NotNull @Positive Integer produtoId,
        @NotNull @Positive BigDecimal quantidadeUtilizada,
        @NotNull @Positive BigDecimal quantidadeReservada
) {
}
