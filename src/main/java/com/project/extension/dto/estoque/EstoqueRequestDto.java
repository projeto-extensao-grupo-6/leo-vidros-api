package com.project.extension.dto.estoque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EstoqueRequestDto(
        @NotNull @Positive Integer produtoId,
        @NotBlank String localizacao,
        @NotNull @Positive BigDecimal quantidadeTotal
) { }

