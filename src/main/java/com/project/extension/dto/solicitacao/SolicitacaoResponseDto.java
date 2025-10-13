package com.project.extension.dto.solicitacao;

public record SolicitacaoResponseDto(

        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone
) {
}
