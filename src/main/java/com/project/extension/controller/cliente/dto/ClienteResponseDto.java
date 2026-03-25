package com.project.extension.controller.cliente.dto;

import com.project.extension.controller.valueobject.endereco.EnderecoResponseDto;

import java.util.List;

public record ClienteResponseDto (
    Integer id,
    String nome,
    String cpf,
    String email,
    String telefone,
    String status,
    List<EnderecoResponseDto> enderecos
){}