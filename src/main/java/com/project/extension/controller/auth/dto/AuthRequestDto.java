package com.project.extension.controller.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank String email,
        @NotBlank String senha
) {
}
