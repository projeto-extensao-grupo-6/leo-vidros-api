package com.project.extension.controller.solicitacao.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequestDto(

        @NotNull String nome,
        @NotNull String email,
        @NotNull String cpf,
        @NotNull String telefone
) {}
