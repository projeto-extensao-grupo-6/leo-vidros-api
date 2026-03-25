package com.project.extension.controller.valueobject.etapa;

import jakarta.validation.constraints.NotBlank;

public record EtapaRequestDto(
        @NotBlank String tipo,
        @NotBlank String nome
) {
}
