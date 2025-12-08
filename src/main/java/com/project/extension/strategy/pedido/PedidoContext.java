package com.project.extension.strategy.pedido;

import com.project.extension.entity.Cliente;
import com.project.extension.entity.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoContext {

    private final PedidoProdutoStrategy produtoStrategy;
    private final PedidoServicoStrategy servicoStrategy;

    private PedidoStrategy resolveStrategy(String tipo) {
        return switch (tipo) {
            case "produto" -> produtoStrategy;
            case "serviço" -> servicoStrategy;
            default -> throw new IllegalStateException("Unexpected value: " + tipo);
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

