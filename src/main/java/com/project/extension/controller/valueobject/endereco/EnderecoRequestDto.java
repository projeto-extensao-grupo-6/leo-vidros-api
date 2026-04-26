package com.project.extension.controller.valueobject.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDto(
        @NotBlank(message = "Rua é obrigatória") String rua,
        String complemento,
        @NotBlank(message = "CEP é obrigatório") String cep,
        @NotBlank(message = "Cidade é obrigatória") String cidade,
        String bairro,
        @NotBlank(message = "UF é obrigatória") String uf,
        @NotBlank(message = "País é obrigatório") String pais,
        @NotNull(message = "Número é obrigatório") Integer numero
) {
}
