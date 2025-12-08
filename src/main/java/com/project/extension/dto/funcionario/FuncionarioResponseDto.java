package com.project.extension.dto.funcionario;

public record FuncionarioResponseDto(
        Integer id,
        String nome,
        String telefone,
        String funcao,
        String contrato,
        String escala,
        Boolean status
) {}
