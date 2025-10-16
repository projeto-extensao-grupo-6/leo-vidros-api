package com.project.extension.dto.pedido;

import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoMapper {

    private final StatusMapper statusMapper;

    public Pedido toEntity(PedidoRequestDto dto) {
        if (dto == null) return null;

        Pedido pedido =  new Pedido(
                dto.valorTotal(),
                dto.ativo(),
                dto.observacao()
        );

        pedido.setStatus(statusMapper.toEntity(dto.status()));

        return pedido;
    }

    public PedidoResponseDto toResponse(Pedido pedido) {
        if (pedido == null) return null;

        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getValorTotal(),
                pedido.getAtivo(),
                pedido.getObservacao(),
                statusMapper.toResponse(pedido.getStatus())
        );
    }
}
