package com.project.extension.strategy.pedido;

import com.project.extension.entity.Pedido;

public interface PedidoStrategy {

    Pedido criar(Pedido pedido);

    Pedido editar(Pedido origem, Pedido destino);

    Pedido deletar(Pedido pedido);
}
