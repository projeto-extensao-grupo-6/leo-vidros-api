package com.project.extension.dto.solicitacao;

import jakarta.validation.constraints.NotNull;

public record CargoDesejadoRequestDto(
        @NotNull
        String cargoDesejado
) {}
