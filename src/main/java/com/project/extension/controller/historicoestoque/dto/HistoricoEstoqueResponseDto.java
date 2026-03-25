package com.project.extension.controller.historicoestoque.dto;

import com.project.extension.controller.estoque.dto.EstoqueResponseDto;
import com.project.extension.controller.pedido.servico.dto.PedidoResponseDto;
import com.project.extension.controller.usuario.dto.UsuarioResponseDto;
import com.project.extension.entity.OrigemMovimentacao;
import com.project.extension.entity.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoEstoqueResponseDto(
        Integer id,
        TipoMovimentacao tipoMovimentacao,
        BigDecimal quantidade,
        BigDecimal quantidadeAtual,
        String observacao,
        OrigemMovimentacao origem,
        LocalDateTime dataHora,
        EstoqueResponseDto estoque,
        UsuarioResponseDto usuario,
        PedidoResponseDto pedido
) {
}
