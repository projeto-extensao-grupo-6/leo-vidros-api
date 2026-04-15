package com.project.extension.controller.pedido.servico.dto.servico;

import jakarta.validation.constraints.NotBlank;

public record ServicoRequestDto(
        @NotBlank String nome,
        @NotBlank String descricao,
        Double precoBase,
        Boolean ativo,
        String etapaNome
) {
}
