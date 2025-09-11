package com.project.extension.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank String email,
        @NotBlank String senha
) {
}
