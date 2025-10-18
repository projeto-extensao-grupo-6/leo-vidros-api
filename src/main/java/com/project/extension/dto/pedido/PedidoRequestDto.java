package com.project.extension.dto.pedido;

import com.project.extension.dto.etapa.EtapaRequestDto;
import com.project.extension.dto.status.StatusRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequestDto(
        @Positive @NotNull BigDecimal valorTotal,
        @NotBlank Boolean ativo,
        @NotBlank String observacao,
        @Valid @NotNull StatusRequestDto status,
        @Valid @NotNull EtapaRequestDto etapa
) {
}
