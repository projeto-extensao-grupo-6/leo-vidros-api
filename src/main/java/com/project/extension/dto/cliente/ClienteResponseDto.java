package com.project.extension.dto.cliente;

import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ClienteResponseDto {
    String nome;
    String cpf;
    String email;
    String senha;
    String telefone;
    Status status;
    List<Endereco> endereco;
}
