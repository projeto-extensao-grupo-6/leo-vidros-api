package com.project.extension.controller.dashboard.dto;

import java.math.BigDecimal;

public record FaturamentoMensalItemDto(
        int mes,
        String nomeMes,
        BigDecimal valor
) {}
