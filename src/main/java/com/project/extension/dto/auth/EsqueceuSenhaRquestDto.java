package com.project.extension.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record EsqueceuSenhaRquestDto(
        @NotBlank String email
) {
}
