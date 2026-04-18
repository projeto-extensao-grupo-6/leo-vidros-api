package com.project.extension.controller.solicitacao;

import com.project.extension.controller.solicitacao.dto.SolicitacaoMapper;
import com.project.extension.controller.solicitacao.dto.SolicitacaoRequestDto;
import com.project.extension.controller.solicitacao.dto.SolicitacaoResponseDto;
import com.project.extension.entity.Solicitacao;
import com.project.extension.service.SecurityLogger;
import com.project.extension.service.SolicitacaoService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Page<SolicitacaoResponseDto>> listarPorNome(
            @RequestParam(required = false)
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{2,50}$", message = "Nome deve conter apenas letras e espaços (2-50 caracteres)")
            String nome,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Authentication authentication) {

        securityLogger.logDataAccess(authentication.getName(), "Solicitacao", null);
        return ResponseEntity.ok(service.listarPorNome(nome, pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Page<SolicitacaoResponseDto>> listar(
            String status,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listar(status, pageable).map(mapper::toResponse));
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
