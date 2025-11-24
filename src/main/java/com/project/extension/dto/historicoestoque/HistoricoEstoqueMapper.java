package com.project.extension.dto.historicoestoque;

import com.project.extension.dto.estoque.EstoqueMapper;
import com.project.extension.dto.usuario.UsuarioMapper;
import com.project.extension.entity.HistoricoEstoque;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HistoricoEstoqueMapper {

    private final UsuarioMapper usuarioMapper;
    private final EstoqueMapper estoqueMapper;

    public HistoricoEstoqueResponseDto toResponse(HistoricoEstoque historicoEstoque) {
        if (historicoEstoque == null) return null;

        return new HistoricoEstoqueResponseDto(
                historicoEstoque.getId(),
                historicoEstoque.getTipoMovimentacao(),
                historicoEstoque.getQuantidade(),
                historicoEstoque.getQuantidadeAtual(),
                historicoEstoque.getObservacao(),
                historicoEstoque.getCreatedAt(),
                estoqueMapper.toResponse(historicoEstoque.getEstoque()),
                usuarioMapper.toResponseDto(historicoEstoque.getUsuario())
        );
    }
}
