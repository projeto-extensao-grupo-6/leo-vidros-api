package com.project.extension.dto.status;

import jakarta.validation.constraints.NotBlank;

public record StatusRequestDto(
        @NotBlank String tipo,
        @NotBlank String nome
) {
}
