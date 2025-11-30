package com.project.extension.strategy.pedido;

import com.project.extension.entity.Pedido;
import com.project.extension.entity.TipoPedido;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoContext {

    private final PedidoProdutoStrategy produtoStrategy;
    private final PedidoServicoStrategy servicoStrategy;

    private PedidoStrategy resolveStrategy(TipoPedido tipo) {
        return switch (tipo) {
            case PRODUTO -> produtoStrategy;
            case SERVICO -> servicoStrategy;
        };
    }

    public Pedido criar(Pedido pedido) {
        return resolveStrategy(pedido.getTipoPedido()).criar(pedido);
    }

    public Pedido editar(Pedido origem, Pedido destino) {
        return resolveStrategy(origem.getTipoPedido()).editar(origem, destino);
    }

    public Pedido deletar(Pedido pedido) {
        return resolveStrategy(pedido.getTipoPedido()).deletar(pedido);
    }
}

