package com.project.extension.strategy.pedido;

import com.project.extension.entity.Pedido;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("SERVICO")
@AllArgsConstructor
@Slf4j
public class PedidoServicoStrategy implements PedidoStrategy{

    @Override
    public Pedido criar(Pedido pedido) {
        return null;
    }

    @Override
    public Pedido editar(Pedido origem, Pedido destino) {
        return null;
    }

    @Override
    public Pedido deletar(Pedido pedido) {
        return null;
    }
}
