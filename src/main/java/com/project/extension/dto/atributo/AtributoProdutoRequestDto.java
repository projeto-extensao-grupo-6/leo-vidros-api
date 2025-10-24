package com.project.extension.dto.atributo;

import jakarta.validation.constraints.NotBlank;

public record AtributoProdutoRequestDto(
        @NotBlank String tipo,
        @NotBlank String valor
) {
}
