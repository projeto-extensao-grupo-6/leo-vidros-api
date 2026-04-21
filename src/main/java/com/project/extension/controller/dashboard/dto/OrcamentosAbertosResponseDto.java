package com.project.extension.controller.dashboard.dto;

import java.math.BigDecimal;

public record OrcamentosAbertosResponseDto(
        int quantidade,
        BigDecimal valorTotal
) {}
