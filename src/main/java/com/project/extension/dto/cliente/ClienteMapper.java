package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoMapper;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Cliente;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class ClienteMapper {

    private final EnderecoMapper enderecoMapper;
    private final StatusMapper statusMapper;

    public Cliente toEntity(ClienteRequestDto dto){
        if(dto == null) return null;

        Cliente cliente = new Cliente(
                dto.nome(),
                dto.cpf(),
                dto.email(),
                dto.senha(),
                dto.telefone()
        );

        cliente.setEnderecos(dto.enderecos().stream().map(enderecoMapper::toEntity).toList());
        cliente.setStatus(statusMapper.toEntity(dto.status()));

        return cliente;
    }

    public ClienteResponseDto toResponse(Cliente cliente) {
        if (cliente == null) return null;

        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                statusMapper.toResponse(cliente.getStatus()),
                cliente.getEnderecos().stream().map(enderecoMapper::toResponse).toList()
        );
    }
}
