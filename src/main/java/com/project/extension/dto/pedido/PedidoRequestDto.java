package com.project.extension.dto.pedido;

import com.project.extension.dto.pedido.produto.ItemPedidoRequestDto;
import com.project.extension.dto.pedido.servico.ServicoRequestDto;

import java.util.List;

public record PedidoRequestDto(
        PedidoBaseRequestDto pedido,
        ServicoRequestDto servico,
        List<ItemPedidoRequestDto> produtos
) {}

