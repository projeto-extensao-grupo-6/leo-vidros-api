package com.project.extension.controller.pedido.servico.dto;

import com.project.extension.controller.cliente.dto.ClienteResponseDto;
import com.project.extension.controller.pedido.servico.dto.produto.ItemPedidoResponseDto;
import com.project.extension.controller.pedido.servico.dto.servico.ServicoResponseDto;
import com.project.extension.controller.valueobject.status.StatusResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponseDto(
        Integer id,
        BigDecimal valorTotal,
        Boolean ativo,
        String descricao,
        String formaPagamento,
        String tipoPedido,
        ClienteResponseDto cliente,
        StatusResponseDto status,
        List<ItemPedidoResponseDto> produtos,
        ServicoResponseDto servico
) {
}
