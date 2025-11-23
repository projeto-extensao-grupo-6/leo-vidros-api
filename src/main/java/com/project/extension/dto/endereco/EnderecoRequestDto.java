package com.project.extension.dto.endereco;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequestDto(
        @NotBlank String rua,
        @NotBlank String complemento,
        @NotBlank String cep,
        @NotBlank String cidade,
        @NotBlank String bairro,
        @NotBlank String uf,
        @NotBlank String pais
        @NotBlank Integer numero
) {
}
