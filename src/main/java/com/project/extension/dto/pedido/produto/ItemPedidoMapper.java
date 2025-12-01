package com.project.extension.dto.pedido.produto;

import com.project.extension.entity.ItemPedido;
import com.project.extension.entity.Pedido;
import com.project.extension.service.EstoqueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemPedidoMapper {

    private final EstoqueService estoqueService;

    public ItemPedido toEntity(ItemPedidoRequestDto dto, Pedido pedido) {
        if (dto == null) return null;

        var estoque = estoqueService.buscarPorId(dto.estoqueId());

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setEstoque(estoque);
        item.setQuantidadeSolicitada(dto.quantidadeSolicitada());
        item.setPrecoUnitarioNegociado(dto.precoUnitarioNegociado());
        item.setObservacao(dto.observacao());

        return item;
    }

    public ItemPedidoResponseDto toResponse(ItemPedido item) {
        if (item == null) return null;

        String nomeProduto = item.getEstoque().getProduto().getNome();

        return new ItemPedidoResponseDto(
                item.getId(),
                item.getEstoque().getId(),
                nomeProduto,
                item.getQuantidadeSolicitada(),
                item.getPrecoUnitarioNegociado(),
                item.getSubtotal(),
                item.getObservacao()
        );
    }
}