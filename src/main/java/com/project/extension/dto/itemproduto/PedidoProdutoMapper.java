package com.project.extension.dto.itemproduto;

import com.project.extension.dto.cliente.ClienteMapper;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.ItemPedido;
import com.project.extension.entity.Pedido;
import com.project.extension.repository.ClienteRepository;
import com.project.extension.repository.EstoqueRepository;
import com.project.extension.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PedidoProdutoMapper {

    private final ClienteRepository clienteRepository;
    private final EstoqueRepository estoqueRepository;
    private final StatusRepository statusRepository;
    private final ClienteMapper clienteMapper;
    private final StatusMapper statusMapper;

    public Pedido toEntity(PedidoProdutoRequestDto dto) {
        if (dto == null) return null;

        var cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        var statusInicial = statusRepository.findByTipoAndNome("PEDIDO", "PENDENTE")
                .orElseThrow(() -> new RuntimeException("Status inicial não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setObservacao(dto.observacao());
        pedido.setStatus(statusInicial); // Seta o status inicial
        pedido.setAtivo(true);

        return pedido;
    }

    public ItemPedido toItemEntity(ItemProdutoRequestDto dto, Pedido pedido) {
        if (dto == null) return null;

        var estoque = estoqueRepository.findById(dto.estoqueId())
                .orElseThrow(() -> new RuntimeException("Item de Estoque não encontrado."));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setEstoque(estoque);
        item.setQuantidadeSolicitada(dto.quantidadeSolicitada());
        item.setPrecoUnitarioNegociado(dto.precoUnitarioNegociado());
        item.setObservacao(dto.observacao());

        return item;
    }

    public ItemPedidoResponseDto toItemResponse(ItemPedido item) {
        if (item == null) return null;

        // Assumindo que a Entity Estoque tem um relacionamento com Produto para pegar o nome
        String nomeProduto = item.getEstoque().getProduto().getNome();

        return new ItemPedidoResponseDto(
                item.getId(),
                item.getEstoque().getId(),
                nomeProduto,
                item.getQuantidadeSolicitada(),
                item.getPrecoUnitarioNegociado(),
                item.getSubtotal(), // O valor STORED do banco de dados
                item.getObservacao()
        );
    }

    public PedidoProdutoResponseDto toResponse(Pedido pedido) {
        if (pedido == null) return null;

        // Mapeia a lista de ItemPedido Entity para a lista de ItemPedidoResponseDto
        List<ItemPedidoResponseDto> itensResponse = pedido.getItensPedido().stream()
                .map(this::toItemResponse)
                .toList();

        return new PedidoProdutoResponseDto(
                pedido.getId(),
                clienteMapper.toResponse(pedido.getCliente()),
                statusMapper.toResponse(pedido.getStatus()),
                pedido.getValorTotal(),
                pedido.getObservacao(),
                itensResponse
        );
    }
}