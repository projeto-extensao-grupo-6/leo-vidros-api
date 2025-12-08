package com.project.extension.dto.historicoestoque;

import com.project.extension.dto.estoque.EstoqueMapper;
import com.project.extension.dto.pedido.PedidoMapper;
import com.project.extension.dto.usuario.UsuarioMapper;
import com.project.extension.entity.HistoricoEstoque;
import com.project.extension.service.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HistoricoEstoqueMapper {

    private final UsuarioMapper usuarioMapper;
    private final EstoqueMapper estoqueMapper;
    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public HistoricoEstoqueResponseDto toResponse(HistoricoEstoque historicoEstoque) {
        if (historicoEstoque == null) return null;

        return new HistoricoEstoqueResponseDto(
                historicoEstoque.getId(),
                historicoEstoque.getTipoMovimentacao(),
                historicoEstoque.getQuantidade(),
                historicoEstoque.getQuantidadeAtual(),
                historicoEstoque.getObservacao(),
                historicoEstoque.getOrigem(),
                historicoEstoque.getDataMovimentacao(),
                estoqueMapper.toResponse(historicoEstoque.getEstoque()),
                usuarioMapper.toResponseDto(historicoEstoque.getUsuario()),
                historicoEstoque.getPedido() != null
                        ? pedidoMapper.toResponse(
                        pedidoService.buscarPorId(historicoEstoque.getPedido().getId())
                )
                        : null
        );
    }
}
