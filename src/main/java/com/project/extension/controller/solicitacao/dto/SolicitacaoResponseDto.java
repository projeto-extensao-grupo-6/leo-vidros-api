package com.project.extension.controller.solicitacao.dto;

import com.project.extension.controller.valueobject.status.StatusResponseDto;

public record SolicitacaoResponseDto(

        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone,
        StatusResponseDto status
) {
}
