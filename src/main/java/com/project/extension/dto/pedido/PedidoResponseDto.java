package com.project.extension.dto.pedido;

import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.dto.etapa.EtapaResponseDto;
import com.project.extension.dto.status.StatusResponseDto;

import java.math.BigDecimal;

public record PedidoResponseDto(
        Integer id,
        BigDecimal valorTotal,
        Boolean ativo,
        String observacao,
        StatusResponseDto status,
        EtapaResponseDto etapa,
        ClienteResponseDto cliente
) {
}
