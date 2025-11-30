package com.project.extension.strategy.pedido;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.ItemPedido;
import com.project.extension.entity.Pedido;
import com.project.extension.service.EstoqueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("PRODUTO")
@AllArgsConstructor
@Slf4j
public class PedidoProdutoStrategy implements PedidoStrategy {

    private final EstoqueService estoqueService;

    @Override
    public Pedido criar(Pedido pedido) {

        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itensProcessados = new ArrayList<>();

        for (ItemPedido item : pedido.getItensPedido()) {

            Estoque estoque = estoqueService.buscarEstoquePorIdProduto(
                    item.getEstoque().getProduto()
            );

            BigDecimal qtd = item.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(estoque.getProduto());
            movimento.setLocalizacao(estoque.getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.saida(movimento);

            item.setPedido(pedido);

            BigDecimal subtotal = qtd.multiply(item.getPrecoUnitarioNegociado());
            item.setSubtotal(subtotal);

            total = total.add(subtotal);
            itensProcessados.add(item);
        }

        pedido.setItensPedido(itensProcessados);
        pedido.setValorTotal(total);

        return pedido;
    }

    @Override
    public Pedido editar(Pedido origem, Pedido destino) {
        for (ItemPedido itemAntigo : origem.getItensPedido()) {

            BigDecimal qtd = itemAntigo.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(itemAntigo.getEstoque().getProduto());
            movimento.setLocalizacao(itemAntigo.getEstoque().getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.entrada(movimento);
        }

        return null;
    }

    @Override
    public Pedido deletar(Pedido pedido) {

        for (ItemPedido item : pedido.getItensPedido()) {

            BigDecimal qtd = item.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(item.getEstoque().getProduto());
            movimento.setLocalizacao(item.getEstoque().getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.entrada(movimento);
        }

        return pedido;
    }
}

