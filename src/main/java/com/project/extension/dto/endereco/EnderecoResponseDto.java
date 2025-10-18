package com.project.extension.dto.endereco;

public record EnderecoResponseDto(
        Integer id,
        String rua,
        String complemento,
        String cep,
        String cidade,
        String bairro,
        Character uf,
        String pais
) {
}
