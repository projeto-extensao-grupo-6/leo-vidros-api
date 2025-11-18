package com.project.extension.dto.usuario;

import com.project.extension.dto.endereco.EnderecoResponseDto;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone,
        Boolean firstLogin
) {}