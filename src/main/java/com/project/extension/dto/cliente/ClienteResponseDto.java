package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoResponseDto;
import com.project.extension.dto.status.StatusResponseDto;

import java.util.List;

public record ClienteResponseDto (
    Integer id,
    String nome,
    String cpf,
    String email,
    String senha,
    StatusResponseDto status,
    List<EnderecoResponseDto> enderecos
){}