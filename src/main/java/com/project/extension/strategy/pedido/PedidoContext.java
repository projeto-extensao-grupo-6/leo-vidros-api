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

    private PedidoStrategy resolveStrategy(Pedido pedido) {
        String tipo = pedido.getTipoPedido();
        if (tipo == null) {
            if (pedido.getServico() != null) return servicoStrategy;
            if (pedido.getItensPedido() != null && !pedido.getItensPedido().isEmpty()) return produtoStrategy;
            return servicoStrategy;
        }
        String t = tipo.toLowerCase().trim()
                .replace("ç", "c")
                .replace("ã", "a");
        return switch (t) {
            case "produto" -> produtoStrategy;
            case "servico", "servi\u00e7o" -> servicoStrategy;
            default -> throw new IllegalStateException("Unexpected tipoPedido: " + tipo);
        };
    }

    public Pedido criar(Pedido pedido) {
        return resolveStrategy(pedido).criar(pedido);
    }

    public Pedido editar(Pedido origem, Pedido destino) {
        return resolveStrategy(origem).editar(origem, destino);
    }

    public Pedido deletar(Pedido pedido) {
        return resolveStrategy(pedido).deletar(pedido);
    }
}

