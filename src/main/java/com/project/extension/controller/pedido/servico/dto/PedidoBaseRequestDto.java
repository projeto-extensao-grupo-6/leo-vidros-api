package com.project.extension.controller.pedido.servico.dto;

import com.project.extension.controller.valueobject.status.StatusRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PedidoBaseRequestDto(
        @DecimalMin("0") @NotNull BigDecimal valorTotal,
        @NotNull Boolean ativo,
        String formaPagamento,
        String observacao,
        @NotNull Integer clienteId,
        String clienteNome,
        @Valid @NotNull StatusRequestDto status
) {
}
