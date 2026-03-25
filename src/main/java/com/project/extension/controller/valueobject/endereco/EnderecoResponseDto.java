package com.project.extension.controller.valueobject.endereco;

public record EnderecoResponseDto(
        Integer id,
        String rua,
        String complemento,
        String cep,
        String cidade,
        String bairro,
        String uf,
        String pais,
        Integer numero
) {
}
