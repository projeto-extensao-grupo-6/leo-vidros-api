package com.project.extension.controller.pedido.servico.dto;

import com.project.extension.controller.pedido.servico.dto.produto.ItemPedidoRequestDto;
import com.project.extension.controller.pedido.servico.dto.servico.ServicoRequestDto;

import java.util.List;

public record PedidoRequestDto(
        PedidoBaseRequestDto pedido,
        ServicoRequestDto servico,
        List<ItemPedidoRequestDto> produtos
) {}

