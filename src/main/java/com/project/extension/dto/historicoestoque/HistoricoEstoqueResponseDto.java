package com.project.extension.dto.historicoestoque;

import com.project.extension.dto.estoque.EstoqueResponseDto;
import com.project.extension.dto.usuario.UsuarioResponseDto;
import com.project.extension.entity.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoEstoqueResponseDto(
        Integer id,
        TipoMovimentacao tipoMovimentacao,
        BigDecimal quantidade,
        BigDecimal quantidadeAtual,
        String observacao,
        LocalDateTime dataHora,
        EstoqueResponseDto estoque,
        UsuarioResponseDto usuario
) {
}
