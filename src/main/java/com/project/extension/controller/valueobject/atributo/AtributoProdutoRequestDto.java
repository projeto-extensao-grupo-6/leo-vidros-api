package com.project.extension.controller.valueobject.atributo;

import jakarta.validation.constraints.NotBlank;

public record AtributoProdutoRequestDto(
        @NotBlank String tipo,
        @NotBlank String valor
) {
}
