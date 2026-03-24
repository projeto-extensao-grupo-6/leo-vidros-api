package com.project.extension.controller.auth.dto;

public record AuthResponseDto(
        String token, // Null quando usando cookies HTTP-only 
        String nome,
        Integer id,
        Boolean firstLogin,
        String email
) {
}

