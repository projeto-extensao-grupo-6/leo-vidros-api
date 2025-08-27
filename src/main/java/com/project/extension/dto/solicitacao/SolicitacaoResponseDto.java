package com.project.extension.dto.solicitacao;

import com.project.extension.dto.usuario.UsuarioResponseDto;

public record SolicitacaoResponseDto(

        Integer id,
        String nome,
        String cpf,
        String email
) {
}
