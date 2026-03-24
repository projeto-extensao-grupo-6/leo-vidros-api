package com.project.extension.controller.usuario.dto;

import com.project.extension.controller.valueobject.endereco.EnderecoResponseDto;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String cpf,
        String email,
        String senha,
        String telefone,
        Boolean firstLogin,
        EnderecoResponseDto endereco
) {}