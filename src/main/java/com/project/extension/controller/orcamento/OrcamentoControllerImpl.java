package com.project.extension.controller.orcamento;

import com.project.extension.controller.orcamento.dto.OrcamentoMapper;
import com.project.extension.controller.orcamento.dto.OrcamentoRequestDto;
import com.project.extension.controller.orcamento.dto.OrcamentoResponseDto;
import com.project.extension.controller.orcamento.dto.ReportDto;
import com.project.extension.entity.Orcamento;
import com.project.extension.rabbitmq.queue.PdfCacheService;
import com.project.extension.service.OrcamentoService;
import com.project.extension.service.OrcamentoSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    private final PdfCacheService pdfCacheService;

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
        String numeroOrcamento = orcamento.getNumeroOrcamento();
        byte[] pdf = numeroOrcamento != null
                ? pdfCacheService.obterPorNumeroOrcamento(numeroOrcamento)
                : null;

        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"orcamento_" + numeroOrcamento + ".pdf\"")
                .body(pdf);
    }

    @GetMapping("/numero/{numeroOrcamento}/pdf")
    public ResponseEntity<?> obterPdfPorNumero(@PathVariable String numeroOrcamento) {
        try {
            byte[] pdf = pdfCacheService.obterPorNumeroOrcamento(numeroOrcamento);

            if (pdf == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"orcamento_" + numeroOrcamento + ".pdf\"")
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/numero/{numeroOrcamento}/report")
    public ResponseEntity<?> obterReportPorNumero(@PathVariable String numeroOrcamento) {
        try {
            byte[] pdf = pdfCacheService.obterPorNumeroOrcamento(numeroOrcamento);
            if (pdf == null) {
                return ResponseEntity.notFound().build();
            }

            ReportDto report = new ReportDto(
                    pdf,
                    MediaType.APPLICATION_PDF_VALUE,
                    "orcamento_" + numeroOrcamento + ".pdf"
            );
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/numero/{numeroOrcamento}/status")
    public ResponseEntity<?> verificarStatusPdfCache(@PathVariable String numeroOrcamento) {
        try {
            byte[] pdf = pdfCacheService.obterPorNumeroOrcamento(numeroOrcamento);

            boolean pronto = pdf != null;
            long tamanho = pronto ? pdf.length : 0;
            String mensagem = pronto ? "PDF pronto para download" : "PDF ainda não foi gerado";

            return ResponseEntity.ok(Map.of(
                    "numeroOrcamento", numeroOrcamento,
                    "pronto", pronto,
                    "tamanho", tamanho,
                    "mensagem", mensagem
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Erro ao verificar status",
                    "mensagem", e.getMessage()
            ));
        }
    }

    @Override
    public SseEmitter streamProgresso(String orcamentoId) {
        return sseService.criarEmitter(orcamentoId);
    }

    @GetMapping("/debug/cache-status")
    public ResponseEntity<?> debugCacheStatus() {
        int tamanho = pdfCacheService.getTamanhoCacheSize();
        java.util.Set<String> chaves = pdfCacheService.obterChavesCache();
        
        
        return ResponseEntity.ok(Map.of(
            "tamanho", tamanho,
            "chaves", chaves,
            "mensagem", tamanho > 0 ? "PDFs em cache" : "Cache vazio"
        ));
    }

    @GetMapping("/debug/teste-pdf")
    public ResponseEntity<?> testePdf() {

        java.util.Set<String> chaves = pdfCacheService.obterChavesCache();
        
        if (chaves.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "status", "sem_pdf",
                "mensagem", "Nenhum PDF em cache"
            ));
        }
        
        // Pega a primeira chave
        String chave = chaves.iterator().next();
        byte[] pdf = pdfCacheService.obterPdf(chave);
        
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"teste_" + chave + "\"")
                .body(pdf);
    }
}
