package com.project.extension.dto.etapa;

import jakarta.validation.constraints.NotBlank;

public record EtapaRequestDto(
        @NotBlank String tipo,
        @NotBlank String nome
) {
}
