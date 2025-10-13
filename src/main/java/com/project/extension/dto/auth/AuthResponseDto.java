package com.project.extension.dto.auth;

public record AuthResponseDto(
        String token,
        String nome,
        Integer id
) {
}

