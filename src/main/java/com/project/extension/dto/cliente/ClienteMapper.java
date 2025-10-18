package com.project.extension.dto.cliente;

import com.project.extension.dto.endereco.EnderecoMapper;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Cliente;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ClienteMapper {

    private final EnderecoMapper enderecoMapper;
    private final StatusMapper statusMapper;

    public Cliente toEntity(ClienteRequestDto dto){
        if(dto == null) return null;

        return new Cliente(
                dto.nome(),
                dto.cpf(),
                dto.email(),
                dto.senha(),
                dto.telefone(),
                statusMapper.toEntity(dto.status()),
                enderecoMapper.toEntity(dto.endereco())
        );
    }

    public List<Cliente> toEntity(List<ClienteRequestDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> new Cliente(
                        dto.nome(),
                        dto.cpf(),
                        dto.email(),
                        dto.senha(),
                        dto.telefone(),
                        dto.status(),
                        dto.endereco()
                ))
                .toList();
    }

    public ClienteResponseDto toResponse(Cliente cliente) {
        if (cliente == null) return null;

        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getStatus(),
                cliente.getEnderecos()
        );
    }

    public List<ClienteResponseDto> toResponse(List<Cliente> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            return Collections.emptyList();
        }

        return clientes.stream()
                .map(cliente -> new ClienteResponseDto(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getEmail(),
                        cliente.getTelefone(),
                        cliente.getStatus(),
                        cliente.getEnderecos()
                ))
                .toList();
    }

}
