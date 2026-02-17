package com.project.extension.controller.solicitacao;

import com.project.extension.dto.solicitacao.SolicitacaoMapper;
import com.project.extension.dto.solicitacao.SolicitacaoRequestDto;
import com.project.extension.dto.solicitacao.SolicitacaoResponseDto;
import com.project.extension.entity.Solicitacao;
import com.project.extension.service.SecurityLogger;
import com.project.extension.service.SolicitacaoService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
@Validated
public class SolicitacaoControllerImpl implements SolicitacaoControllerDoc{

    private final SolicitacaoMapper mapper;
    private final SolicitacaoService service;
    private final SecurityLogger securityLogger;

    @Override
    public ResponseEntity<SolicitacaoResponseDto> cadastrarSolicitacao(SolicitacaoRequestDto dto) {
        securityLogger.logSecurityEvent("SOLICITACAO_CREATED", "IP: Request from public endpoint");
        Solicitacao solicitacao = mapper.toEntity(dto);
        Solicitacao cadastrado = service.cadastrar(solicitacao);
        return ResponseEntity.status(201).body(mapper.toResponse(cadastrado));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SolicitacaoResponseDto>> listarPorNome(
            @RequestParam(required = false) 
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{2,50}$", message = "Nome deve conter apenas letras e espaços (2-50 caracteres)")
            String nome, 
            Authentication authentication) {
        
        securityLogger.logDataAccess(authentication.getName(), "Solicitacao", null);
        List<Solicitacao> pendentes = service.listarPorNome(nome);
        if (pendentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SolicitacaoResponseDto> dtos = pendentes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<List<SolicitacaoResponseDto>> listar(String status) {
        List<Solicitacao> pendentes = service.listar(status);
        if (pendentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SolicitacaoResponseDto> dtos = pendentes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<Void> aceitarSolicitacao( @PathVariable Integer id) {
        service.aceitarSolicitacao(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> recusarSolicitacao(Integer id) {
        service.recusarSolicitacao(id);
        return ResponseEntity.ok().build();
    }
}
