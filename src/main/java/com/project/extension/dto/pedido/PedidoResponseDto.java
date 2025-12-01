package com.project.extension.dto.pedido;

import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.dto.pedido.produto.ItemPedidoResponseDto;
import com.project.extension.dto.pedido.servico.ServicoResponseDto;
import com.project.extension.dto.status.StatusResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponseDto(
        Integer id,
        BigDecimal valorTotal,
        Boolean ativo,
        String observacao,
        String formaPagamento,
        String tipoPedido,
        ClienteResponseDto cliente,
        StatusResponseDto status,
        List<ItemPedidoResponseDto> produtos,
        ServicoResponseDto servico
) {
}
