package com.project.extension.controller.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record EsqueceuSenhaRquestDto(
        @NotBlank String email
) {
}
