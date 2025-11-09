package com.project.extension.dto.solicitacao;

import com.project.extension.dto.status.StatusResponseDto;

public record SolicitacaoResponseDto(

        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone,
        StatusResponseDto status
) {
}
