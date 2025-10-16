package com.project.extension.dto.cliente;

import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ClienteRequestDto {
    @NotBlank String nome;
    @NotBlank String cpf;
    @NotBlank String email;
    @NotBlank String senha;
    @NotBlank String telefone;
    @NotBlank Status status;
    @NotBlank List<Endereco> endereco;
}
