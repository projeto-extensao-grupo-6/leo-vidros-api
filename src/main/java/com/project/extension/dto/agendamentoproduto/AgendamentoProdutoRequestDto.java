package com.project.extension.dto.agendamentoproduto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AgendamentoProdutoRequestDto(
        @NotNull @Positive Integer produtoId,
        @NotNull @Positive Integer quantidadeUtilizada,
        @NotNull @Positive Integer quantidadeReservada
) {
}
