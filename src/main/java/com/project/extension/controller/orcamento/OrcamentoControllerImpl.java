package com.project.extension.controller.orcamento;

import com.project.extension.dto.orcamento.OrcamentoMapper;
import com.project.extension.dto.orcamento.OrcamentoRequestDto;
import com.project.extension.dto.orcamento.OrcamentoResponseDto;
import com.project.extension.entity.Orcamento;
import com.project.extension.service.OrcamentoService;
import com.project.extension.service.OrcamentoSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
@Slf4j
public class OrcamentoControllerImpl implements OrcamentoControllerDoc {

    private final OrcamentoService service;
    private final OrcamentoMapper mapper;
    private final OrcamentoSseService sseService;

    @Value("${app.orcamento.diretorio:./storage/orcamentos}")
    private String diretorioPdf;

    @Override
    public ResponseEntity<OrcamentoResponseDto> criar(OrcamentoRequestDto request) {
        Orcamento salvo = service.criarEGerarPdf(request);
        return ResponseEntity.status(201).body(mapper.toResponse(salvo));
    }

    @Override
    public ResponseEntity<OrcamentoResponseDto> buscarPorId(Integer id) {
        Orcamento orcamento = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(orcamento));
    }

    @Override
    public ResponseEntity<List<OrcamentoResponseDto>> listar() {
        List<Orcamento> orcamentos = service.listar();
        if (orcamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orcamentos.stream().map(mapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<OrcamentoResponseDto>> listarPorPedido(Integer pedidoId) {
        List<Orcamento> orcamentos = service.listarPorPedido(pedidoId);
        if (orcamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orcamentos.stream().map(mapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<OrcamentoResponseDto> atualizarStatus(Integer id, Map<String, String> body) {
        String statusNome = body.getOrDefault("status", "ENVIADO");
        String pdfPath = body.get("pdfPath");
        Orcamento atualizado = service.atualizarStatus(id, statusNome, pdfPath);
        return ResponseEntity.ok(mapper.toResponse(atualizado));
    }

    @Override
    public ResponseEntity<?> baixarPdf(Integer id) {
        Orcamento orcamento = service.buscarPorId(id);

        if (orcamento.getPdfPath() == null || orcamento.getPdfPath().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(diretorioPdf).resolve(orcamento.getPdfPath());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            log.warn("Arquivo PDF não encontrado no disco: {}", filePath);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + orcamento.getPdfPath() + "\"")
                .body(resource);
    }

    @Override
    public SseEmitter streamProgresso(String orcamentoId) {
        return sseService.criarEmitter(orcamentoId);
    }
}
