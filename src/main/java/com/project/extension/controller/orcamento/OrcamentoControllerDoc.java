package com.project.extension.controller.orcamento;

import com.project.extension.controller.orcamento.dto.OrcamentoRequestDto;
import com.project.extension.controller.orcamento.dto.OrcamentoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Tag(name = "Orçamentos", description = "Operações relacionadas à geração e gerenciamento de orçamentos")
public interface OrcamentoControllerDoc {

    @PostMapping
    @Operation(summary = "Criar orçamento e gerar PDF", description = """
            Cria um novo orçamento, salva no banco de dados e publica uma mensagem
            na fila RabbitMQ para geração assíncrona do PDF.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orçamento criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrcamentoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos",
                    content = @Content())
    })
    ResponseEntity<OrcamentoResponseDto> criar(@RequestBody OrcamentoRequestDto request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orçamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrcamentoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado",
                    content = @Content())
    })
    ResponseEntity<OrcamentoResponseDto> buscarPorId(@PathVariable Integer id);

    @GetMapping
    @Operation(summary = "Listar todos os orçamentos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamentos encontrados"),
            @ApiResponse(responseCode = "204", description = "Nenhum orçamento encontrado",
                    content = @Content())
    })
    ResponseEntity<List<OrcamentoResponseDto>> listar();

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Listar orçamentos por pedido")
    ResponseEntity<List<OrcamentoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId);

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do orçamento", description = """
            Atualiza o status do orçamento e opcionalmente o caminho do PDF.
            Chamado pelo microserviço após gerar o PDF.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado",
                    content = @Content())
    })
    ResponseEntity<OrcamentoResponseDto> atualizarStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    );

        @GetMapping("/id/{id}/pdf")
        @Operation(summary = "Baixar PDF do orçamento por ID (conteúdo vindo da fila/cache)")
    ResponseEntity<?> baixarPdf(@PathVariable Integer id);

        @GetMapping("/numero/{numeroOrcamento}/pdf")
        @Operation(summary = "Baixar PDF do orçamento por número (conteúdo vindo da fila/cache)")
        ResponseEntity<?> obterPdfPorNumero(@PathVariable String numeroOrcamento);

        @GetMapping("/numero/{numeroOrcamento}/report")
        @Operation(summary = "Obter ReportDto com conteúdo do PDF")
        ResponseEntity<?> obterReportPorNumero(@PathVariable String numeroOrcamento);

        @GetMapping("/numero/{numeroOrcamento}/status")
        @Operation(summary = "Consultar status de geração do PDF no cache")
        ResponseEntity<?> verificarStatusPdfCache(@PathVariable String numeroOrcamento);

    @GetMapping("/stream/{orcamentoId}")
    @Operation(summary = "Stream SSE de progresso da geração do orçamento")
    SseEmitter streamProgresso(@PathVariable String orcamentoId);
}
