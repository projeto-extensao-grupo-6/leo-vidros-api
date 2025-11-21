package com.project.extension.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DefinirSenhaRequestDto(

        @NotNull(message = "O ID do usuário é obrigatório.")
        Integer idUsuario,

        @NotBlank(message = "A nova senha é obrigatória.")
        String novaSenha
) {}