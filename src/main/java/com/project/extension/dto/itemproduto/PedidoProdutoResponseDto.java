package com.project.extension.dto.itemproduto;

import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.dto.status.StatusResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoProdutoResponseDto(
        Integer id,
        ClienteResponseDto cliente,
        StatusResponseDto status,
        BigDecimal valorTotal,
        String observacao,
        List<ItemPedidoResponseDto> itensPedido
) {
}
