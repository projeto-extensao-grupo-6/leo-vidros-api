package com.project.extension.dto.usuario;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone
) {}