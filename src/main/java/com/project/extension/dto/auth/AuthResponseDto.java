package com.project.extension.dto.auth;

public record AuthResponseDto(
        String token, // Null quando usando cookies HTTP-only 
        String nome,
        Integer id,
        Boolean firstLogin,
        String email
) {
}

