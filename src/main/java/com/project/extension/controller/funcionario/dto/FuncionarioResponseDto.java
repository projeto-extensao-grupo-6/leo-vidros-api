package com.project.extension.controller.funcionario.dto;

public record FuncionarioResponseDto(
        Integer id,
        String nome,
        String telefone,
        String funcao,
        String contrato,
        String escala,
        Boolean status
) {}
