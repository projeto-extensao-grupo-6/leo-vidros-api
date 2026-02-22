package com.project.extension.dto.funcionario;

public record FuncionarioDisponivelResponseDto(
        Integer id,
        String nome,
        String telefone,
        String funcao,
        String escala,
        Boolean status
) {}
