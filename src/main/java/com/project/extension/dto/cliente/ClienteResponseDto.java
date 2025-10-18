package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoResponseDto;
import com.project.extension.dto.status.StatusResponseDto;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;

import java.util.List;

public record ClienteResponseDto (
    Integer id,
    String nome,
    String cpf,
    String email,
    String senha,
    String telefone,
    StatusResponseDto status,
    EnderecoResponseDto endereco
){

}
