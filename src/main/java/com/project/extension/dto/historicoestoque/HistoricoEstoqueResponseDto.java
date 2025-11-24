package com.project.extension.dto.historicoestoque;

import com.project.extension.dto.estoque.EstoqueResponseDto;
import com.project.extension.dto.usuario.UsuarioResponseDto;
import com.project.extension.entity.TipoMovimentacao;

import java.time.LocalDateTime;

public record HistoricoEstoqueResponseDto(
        Integer id,
        TipoMovimentacao tipoMovimentacao,
        Integer quantidade,
        Integer quantidadeAtual,
        String observacao,
        LocalDateTime dataHora,
        EstoqueResponseDto estoque,
        UsuarioResponseDto usuario
) {
}
