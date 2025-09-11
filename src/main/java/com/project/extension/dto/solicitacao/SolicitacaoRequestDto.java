package com.project.extension.dto.solicitacao;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequestDto(

        @NotNull
        String nome,

        @NotNull
        String email,

        @NotNull
        String cpf
) {}
