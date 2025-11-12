package com.project.extension.controller.solicitacao;

import com.project.extension.dto.solicitacao.SolicitacaoMapper;
import com.project.extension.dto.solicitacao.SolicitacaoRequestDto;
import com.project.extension.dto.solicitacao.SolicitacaoResponseDto;
import com.project.extension.entity.Solicitacao;
import com.project.extension.service.SolicitacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoControllerImpl implements SolicitacaoControllerDoc{

    private final SolicitacaoMapper mapper;
    private final SolicitacaoService service;

    @Override
    public ResponseEntity<SolicitacaoResponseDto> cadastrarSolicitacao(SolicitacaoRequestDto dto) {
        Solicitacao solicitacao = mapper.toEntity(dto);
        Solicitacao cadastrado = service.cadastrar(solicitacao);
        return ResponseEntity.status(201).body(mapper.toResponse(cadastrado));
    }

    @Override
    public ResponseEntity<List<SolicitacaoResponseDto>> listarPorNome(@RequestParam(required = false) String nome) {
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
