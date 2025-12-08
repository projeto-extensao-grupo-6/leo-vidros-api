package com.project.extension.dto.pedido;

import com.project.extension.dto.cliente.ClienteMapper;
import com.project.extension.dto.pedido.produto.ItemPedidoMapper;
import com.project.extension.dto.pedido.produto.ItemPedidoRequestDto;
import com.project.extension.dto.pedido.servico.ServicoMapper;
import com.project.extension.dto.pedido.servico.ServicoRequestDto;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PedidoMapper {

    private final StatusMapper statusMapper;
    private final ClienteMapper clienteMapper;
    private final ServicoMapper servicoMapper;
    private final ItemPedidoMapper produtoMapper;

    public Pedido toEntity(PedidoRequestDto request) {
        if (request == null) return null;

        Pedido pedido = fromBase(request.pedido());

        if (request.servico() != null) {
            return mapServico(request.servico(), pedido);
        }

        if (request.produtos() != null) {
            return mapProduto(request.produtos(), pedido);
        }

        throw new IllegalArgumentException("Estrutura inválida: pedido precisa ser SERVICO ou PRODUTO.");
    }

    private Pedido mapServico(ServicoRequestDto dto, Pedido pedido) {
        pedido.setTipoPedido("serviço");

        var servicoEntity = servicoMapper.toEntity(dto);

        servicoEntity.setPedido(pedido);
        pedido.setServico(servicoEntity);

        return pedido;
    }
    private Pedido mapProduto(List<ItemPedidoRequestDto> itensDto, Pedido pedido) {
        pedido.setTipoPedido("produto");

        var itens = itensDto.stream()
                .map(itemDto -> produtoMapper.toEntity(itemDto, pedido))
                .toList();

        pedido.setItensPedido(itens);

        return pedido;
    }

    private Pedido fromBase(PedidoBaseRequestDto dto) {
        Pedido p = new Pedido();
        p.setValorTotal(dto.valorTotal());
        p.setAtivo(dto.ativo());
        p.setObservacao(dto.observacao());
        p.setFormaPagamento(dto.formaPagamento());
        p.setCliente(clienteMapper.toEntity(dto.cliente()));
        p.setStatus(statusMapper.toEntity(dto.status()));
        return p;
    }

    public PedidoResponseDto toResponse(Pedido pedido) {
        if (pedido == null) return null;

        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getValorTotal(),
                pedido.getAtivo(),
                pedido.getObservacao(),
                pedido.getFormaPagamento(),
                pedido.getTipoPedido(),
                clienteMapper.toResponse(pedido.getCliente()),
                statusMapper.toResponse(pedido.getStatus()),
                pedido.getItensPedido().stream()
                        .map(produtoMapper::toResponse)
                        .toList(),
                servicoMapper.toResponse(pedido.getServico())
        );
    }
}