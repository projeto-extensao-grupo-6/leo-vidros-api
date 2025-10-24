package com.project.extension.controller.cliente;

import com.project.extension.dto.cliente.ClienteMapper;
import com.project.extension.dto.cliente.ClienteRequestDto;
import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.entity.Cliente;
import com.project.extension.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteControllerImpl implements ClienteControllerDoc {

    private final ClienteService service;
    private final ClienteMapper mapper;

    @Override
    public ResponseEntity<ClienteResponseDto> salvar(ClienteRequestDto request) {
        Cliente cliente = mapper.toEntity(request);
        Cliente clienteSalvo = service.cadastrar(cliente);
        return ResponseEntity.status(201).body(mapper.toResponse(clienteSalvo));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> buscarPorId(Integer id) {
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(cliente));
    }

    @Override
    public ResponseEntity<List<ClienteResponseDto>> buscarTodos() {
        List<Cliente> clientes = service.listar();

        return clientes.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(clientes.stream()
                .map(mapper::toResponse)
                .toList());
    }

    @Override
    public ResponseEntity<ClienteResponseDto> atualizar(ClienteRequestDto request, Integer id) {
        Cliente cliente = mapper.toEntity(request);
        Cliente clienteAtualizado = service.atualizar(cliente, id);
        return ResponseEntity.status(200).body(mapper.toResponse(clienteAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Cliente e vínculos removidos com sucesso.");
    }
}