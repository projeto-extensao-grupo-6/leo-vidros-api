package com.project.extension.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDto(
        @NotBlank String rua,
        @NotBlank String complemento,
        @NotBlank String cep,
        @NotBlank String cidade,
        @NotBlank String bairro,
        @NotBlank String uf,
        @NotBlank String pais,
        @NotNull Integer numero
) {
}
