package com.project.extension.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDto(

        @NotBlank
        String nome
) {
}
