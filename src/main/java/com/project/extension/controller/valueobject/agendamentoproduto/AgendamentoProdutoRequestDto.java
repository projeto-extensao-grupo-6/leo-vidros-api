package com.project.extension.controller.valueobject.agendamentoproduto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AgendamentoProdutoRequestDto(
        @NotNull @Positive Integer produtoId,
        @NotNull @PositiveOrZero BigDecimal quantidadeUtilizada,
        @NotNull @Positive BigDecimal quantidadeReservada
) {
}
