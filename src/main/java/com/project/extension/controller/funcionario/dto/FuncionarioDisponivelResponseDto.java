package com.project.extension.controller.funcionario.dto;

public record FuncionarioDisponivelResponseDto(
        Integer id,
        String nome,
        String telefone,
        String funcao,
        String escala,
        Boolean status
) {}
