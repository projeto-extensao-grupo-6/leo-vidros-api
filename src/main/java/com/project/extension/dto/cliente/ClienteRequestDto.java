package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoRequestDto;
import com.project.extension.dto.status.StatusRequestDto;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClienteRequestDto (
    @NotBlank String nome,
    @NotBlank String cpf,
    @NotBlank String email,
    @NotBlank String telefone,
    @NotBlank String status,
    @Valid @NotNull List<EnderecoRequestDto> enderecos
) {}
