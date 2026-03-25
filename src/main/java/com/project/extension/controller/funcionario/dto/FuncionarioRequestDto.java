package com.project.extension.controller.funcionario.dto;

import jakarta.validation.constraints.NotBlank;

public record FuncionarioRequestDto(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank String funcao,
        @NotBlank String contrato,
        @NotBlank String escala,
        @NotBlank Boolean status
) {}
