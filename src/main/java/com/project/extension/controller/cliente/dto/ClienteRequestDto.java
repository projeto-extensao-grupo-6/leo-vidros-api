package com.project.extension.controller.cliente.dto;

import com.project.extension.controller.valueobject.endereco.EnderecoRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClienteRequestDto (
    @NotBlank String nome,
    String cpf,
    String email,
    String telefone,
    @NotBlank String status,
    @Valid List<EnderecoRequestDto> enderecos
) {}
