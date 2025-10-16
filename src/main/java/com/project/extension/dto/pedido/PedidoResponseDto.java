package com.project.extension.dto.pedido;

import com.project.extension.dto.status.StatusResponseDto;

import java.math.BigDecimal;

public record PedidoResponseDto(
        Integer id,
        BigDecimal valorTotal,
        Boolean ativo,
        String observacao,
        StatusResponseDto status
) {
}
