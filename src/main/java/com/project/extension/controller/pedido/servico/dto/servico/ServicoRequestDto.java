package com.project.extension.controller.pedido.servico.dto.servico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicoRequestDto(
        @NotBlank String nome,
        @NotBlank String descricao,
        Double precoBase,
        Boolean ativo,
        @Valid @NotNull Integer etapaId
) {
}
