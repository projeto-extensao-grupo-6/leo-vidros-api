package com.project.extension.controller.dashboard.dto;

import java.math.BigDecimal;

public record FaturamentoMesResponseDto(
        BigDecimal faturamentoMes,
        Double percentualVariacao
) {}
