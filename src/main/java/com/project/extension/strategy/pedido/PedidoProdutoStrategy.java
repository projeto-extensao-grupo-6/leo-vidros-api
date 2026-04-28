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

        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("Pedido deve conter um cliente.");
        }

        if (pedido.getCliente().getId() == null || pedido.getCliente().getId() == 0) {
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

        // Preserva os itens vindos do request e limpa a coleção antes do primeiro save
        // para evitar cascade de itens sem pedido_id definido (owning side nulo).
        List<ItemPedido> itensRequisicao = new ArrayList<>(pedido.getItensPedido());
        pedido.getItensPedido().clear();

        pedido = pedidoRepository.save(pedido);

        for (ItemPedido item : itensRequisicao) {

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
            pedido.getItensPedido().add(item);
        }

        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
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

        origem.getItensPedido().clear();

        origem.setValorTotal(destino.getValorTotal());
        origem.setAtivo(destino.getAtivo());
        origem.setObservacao(destino.getObservacao());
        origem.setFormaPagamento(destino.getFormaPagamento());
        origem.setTipoPedido(destino.getTipoPedido());
        Status status = statusService.buscarPorTipoAndStatus(
                destino.getStatus().getTipo(),
                destino.getStatus().getNome()
        );
        origem.setStatus(status);
        if (destino.getCliente() != null && destino.getCliente().getId() != null) {
            Cliente clienteAtual = clienteService.buscarPorId(destino.getCliente().getId());
            if (destino.getCliente().getNome() != null && !destino.getCliente().getNome().isBlank()
                    && !destino.getCliente().getNome().equals(clienteAtual.getNome())) {
                clienteAtual.setNome(destino.getCliente().getNome());
                clienteService.atualizar(clienteAtual, clienteAtual.getId());
            }
            origem.setCliente(clienteAtual);
        }

        for (ItemPedido novoItem : destino.getItensPedido()) {

            novoItem.setPedido(origem);

            Estoque movimento = new Estoque();
            movimento.setProduto(novoItem.getEstoque().getProduto());
            movimento.setLocalizacao(novoItem.getEstoque().getLocalizacao());
            movimento.setQuantidadeTotal(novoItem.getQuantidadeSolicitada());

            estoqueService.saida(movimento, origem);

            origem.getItensPedido().add(novoItem);
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

        pedido.setCliente(null);
        pedido.setStatus(null);

        return pedido;
    }
}
