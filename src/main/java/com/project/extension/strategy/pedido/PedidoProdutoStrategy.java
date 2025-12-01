package com.project.extension.strategy.pedido;

import com.project.extension.entity.*;
import com.project.extension.service.ClienteService;
import com.project.extension.service.EstoqueService;
import com.project.extension.service.StatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("PEDIDO_PRODUTO")
@AllArgsConstructor
@Slf4j
public class PedidoProdutoStrategy implements PedidoStrategy {

    private final EstoqueService estoqueService;
    private final StatusService statusService;
    private final ClienteService clienteService;

    @Override
    public Pedido criar(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itensProcessados = new ArrayList<>();

        if (pedido.getCliente() != null) {
            Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
            pedido.setCliente(cliente);
        }

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

        Status status = statusService.buscarPorTipoAndStatus(pedido.getStatus().getTipo(), pedido.getStatus().getNome());
        pedido.setStatus(status);

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

