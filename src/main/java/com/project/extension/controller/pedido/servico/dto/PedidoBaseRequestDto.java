package com.project.extension.controller.pedido.servico.dto;

import com.project.extension.controller.cliente.dto.ClienteResponseDto;
import com.project.extension.controller.valueobject.status.StatusRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoBaseRequestDto(
        @Positive @NotNull BigDecimal valorTotal,
        @NotNull Boolean ativo,
        @NotBlank String formaPagamento,
        @NotBlank String observacao,
        @Valid @NotNull ClienteResponseDto cliente,
        @Valid @NotNull StatusRequestDto status
) {
}
