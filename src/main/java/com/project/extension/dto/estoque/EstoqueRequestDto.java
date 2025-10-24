package com.project.extension.dto.estoque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EstoqueRequestDto(
        @NotNull @Positive Integer produtoId,
        @NotBlank String localizacao,
        @NotNull @Positive Integer quantidade
) { }

