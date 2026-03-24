package com.project.extension.controller.valueobject.status;

import jakarta.validation.constraints.NotBlank;

public record StatusRequestDto(
        @NotBlank String tipo,
        @NotBlank String nome
) {
}
