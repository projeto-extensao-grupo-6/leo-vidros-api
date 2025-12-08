package com.project.extension.strategy.pedido;

import com.project.extension.entity.*;
import com.project.extension.repository.PedidoRepository;
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
    private final PedidoRepository pedidoRepository;

    @Override
    public Pedido criar(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itensProcessados = new ArrayList<>();

        if (pedido.getCliente().getId() == 0 ) {
            Cliente cliente = clienteService.cadastrar(pedido.getCliente());
            pedido.setCliente(cliente);
        } else {
            Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
            pedido.setCliente(cliente);
        }

        Status status = statusService.buscarPorTipoAndStatus(
                pedido.getStatus().getTipo(),
                pedido.getStatus().getNome()
        );
        pedido.setStatus(status);

        pedido = pedidoRepository.save(pedido);

        for (ItemPedido item : pedido.getItensPedido()) {

            Estoque estoque = estoqueService.buscarEstoquePorIdProduto(
                    item.getEstoque().getProduto()
            );

            BigDecimal qtd = item.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(estoque.getProduto());
            movimento.setLocalizacao(estoque.getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.saida(movimento, pedido);

            item.setPedido(pedido);

            BigDecimal subtotal = qtd.multiply(item.getPrecoUnitarioNegociado());
            item.setSubtotal(subtotal);

            total = total.add(subtotal);
            itensProcessados.add(item);
        }

        pedido.setItensPedido(itensProcessados);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }
    @Override
    public Pedido editar(Pedido origem, Pedido destino) {
        // Return old items to inventory
        for (ItemPedido itemAntigo : origem.getItensPedido()) {

            BigDecimal qtd = itemAntigo.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(itemAntigo.getEstoque().getProduto());
            movimento.setLocalizacao(itemAntigo.getEstoque().getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.entrada(movimento);
        }

        // Process new items
        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itensProcessados = new ArrayList<>();

        for (ItemPedido item : destino.getItensPedido()) {

            Estoque estoque = estoqueService.buscarEstoquePorIdProduto(
                    item.getEstoque().getProduto()
            );

            BigDecimal qtd = item.getQuantidadeSolicitada();

            Estoque movimento = new Estoque();
            movimento.setProduto(estoque.getProduto());
            movimento.setLocalizacao(estoque.getLocalizacao());
            movimento.setQuantidadeTotal(qtd);

            estoqueService.saida(movimento, origem);

            item.setPedido(origem);

            BigDecimal subtotal = qtd.multiply(item.getPrecoUnitarioNegociado());
            item.setSubtotal(subtotal);

            total = total.add(subtotal);
            itensProcessados.add(item);
        }

        // Update origem with new data
        origem.setItensPedido(itensProcessados);
        origem.setValorTotal(total);

        // Update status if provided
        if (destino.getStatus() != null) {
            Status status = statusService.buscarPorTipoAndStatus(
                    destino.getStatus().getTipo(),
                    destino.getStatus().getNome()
            );
            origem.setStatus(status);
        }

        // Update cliente if provided
        if (destino.getCliente() != null) {
            if (destino.getCliente().getId() == 0) {
                Cliente cliente = clienteService.cadastrar(destino.getCliente());
                origem.setCliente(cliente);
            } else {
                Cliente cliente = clienteService.buscarPorId(destino.getCliente().getId());
                origem.setCliente(cliente);
            }
        }

        return origem;
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
