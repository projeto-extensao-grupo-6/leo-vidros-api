package com.project.extension.controller.valueobject.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDto(
        @NotBlank String rua,
        String complemento,
        @NotBlank String cep,
        @NotBlank String cidade,
        String bairro,
        @NotBlank String uf,
        @NotBlank String pais,
        @NotNull Integer numero
) {
}
