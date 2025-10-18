package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoRequestDto;
import com.project.extension.dto.status.StatusRequestDto;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClienteRequestDto (
    @NotNull String nome,
    @NotNull String cpf,
    @NotNull String email,
    @NotNull String senha,
    @NotNull String telefone,
    @NotNull StatusRequestDto status,
    @NotNull EnderecoRequestDto endereco
) {

}
