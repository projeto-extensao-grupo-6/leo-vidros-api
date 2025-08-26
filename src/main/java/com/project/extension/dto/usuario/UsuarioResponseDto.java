package com.project.extension.dto.usuario;

public record UsuarioResponseDto(
        Integer id,
        String nome,
        String cpf,
        String email,
        RoleResponseDto role
) {
    public record RoleResponseDto(
            Integer id,
            String nome
    ) {}
}