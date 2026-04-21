package com.project.extension.controller.dashboard.dto;

import java.util.List;

public record FaturamentoAnualResponseDto(
        int ano,
        List<FaturamentoMensalItemDto> meses
) {}
