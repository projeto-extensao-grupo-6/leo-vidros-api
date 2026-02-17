package com.project.extension.controller.cliente;

import com.project.extension.dto.cliente.ClienteMapper;
import com.project.extension.dto.cliente.ClienteRequestDto;
import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.entity.Cliente;
import com.project.extension.service.ClienteService;
import com.project.extension.service.SecurityService;
import com.project.extension.service.SecurityLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteControllerImpl implements ClienteControllerDoc {

    private final ClienteService service;
    private final ClienteMapper mapper;
    private final SecurityService securityService;
    private final SecurityLogger securityLogger;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDto> salvar(ClienteRequestDto request, Authentication authentication) {
        securityLogger.logDataModification(authentication.getName(), "Cliente", "CREATE");
        Cliente cliente = mapper.toEntity(request);
        Cliente clienteSalvo = service.cadastrar(cliente);
        return ResponseEntity.status(201).body(mapper.toResponse(clienteSalvo));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDto> buscarPorId(Integer id, Authentication authentication) {
        securityService.validateResourceAccess(id, "READ");
        securityLogger.logDataAccess(authentication.getName(), "Cliente", id);
        
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(cliente));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ClienteResponseDto>> buscarTodos(Authentication authentication) {
        securityLogger.logDataAccess(authentication.getName(), "Cliente", null);
        List<Cliente> clientes = service.listar();

        return clientes.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(clientes.stream()
                .map(mapper::toResponse)
                .toList());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDto> atualizar(ClienteRequestDto request, Integer id, Authentication authentication) {
        securityService.validateResourceAccess(id, "UPDATE");
        securityLogger.logDataModification(authentication.getName(), "Cliente", "UPDATE::" + id);
        
        Cliente cliente = mapper.toEntity(request);
        Cliente clienteAtualizado = service.atualizar(cliente, id);
        return ResponseEntity.status(200).body(mapper.toResponse(clienteAtualizado));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deletar(Integer id, Authentication authentication) {
        securityService.validateResourceAccess(id, "DELETE");
        securityLogger.logDataModification(authentication.getName(), "Cliente", "DELETE::" + id);
        
        service.deletar(id);
        return ResponseEntity.ok("Cliente e vínculos removidos com sucesso.");
    }
}