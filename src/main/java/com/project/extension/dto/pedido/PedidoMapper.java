package com.project.extension.dto.pedido;

import com.project.extension.dto.etapa.EtapaMapper;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoMapper {

    private final StatusMapper statusMapper;
    private final EtapaMapper etapaMapper;

    public Pedido toEntity(PedidoRequestDto dto) {
        if (dto == null) return null;

        Pedido pedido =  new Pedido(
                dto.valorTotal(),
                dto.ativo(),
                dto.observacao()
        );

        pedido.setStatus(statusMapper.toEntity(dto.status()));
        pedido.setEtapa(etapaMapper.toEntity(dto.etapa()));

        return pedido;
    }

    public Pedido toEntity(PedidoResponseDto dto) {
        if (dto == null) return null;

        Pedido pedido = new Pedido(
                dto.valorTotal(),
                dto.ativo(),
                dto.observacao()
        );

        pedido.setId(dto.id());

        if (dto.status() != null) {
            pedido.setStatus(statusMapper.toEntity(dto.status()));
        }

        if (dto.etapa() != null) {
            pedido.setEtapa(etapaMapper.toEntity(dto.etapa()));
        }

        return pedido;
    }



    public PedidoResponseDto toResponse(Pedido pedido) {
        if (pedido == null) return null;

        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getValorTotal(),
                pedido.getAtivo(),
                pedido.getObservacao(),
                statusMapper.toResponse(pedido.getStatus()),
                etapaMapper.toResponse(pedido.getEtapa())
        );
    }
}
