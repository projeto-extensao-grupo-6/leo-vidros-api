package com.project.extension.dto.usuario;

import com.project.extension.dto.endereco.EnderecoRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDto(

        @NotNull String nome,
        @NotNull String email,
        @NotNull String cpf,
        @NotNull String senha,
        @NotNull String telefone,
        @Valid EnderecoRequestDto endereco
        ) {}
