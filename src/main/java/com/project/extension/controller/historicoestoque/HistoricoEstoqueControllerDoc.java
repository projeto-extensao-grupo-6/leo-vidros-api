package com.project.extension.controller.historicoestoque;

import com.project.extension.controller.historicoestoque.dto.HistoricoEstoqueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Históricos de Estoques", description = "Operações relacionadas à visualização de histórico de movimentações de estoque")
public interface HistoricoEstoqueControllerDoc {

    @GetMapping
    @Operation(summary = "Listar históricos de estoque", description = """
           Listar históricos de estoque
           ---
           Retorna todos os registros de movimentações realizadas no estoque, incluindo entradas e saídas de produtos.
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de históricos de estoque (vazia se não houver registros)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoEstoqueResponseDto.class)
                    ))
    })
    ResponseEntity<Page<HistoricoEstoqueResponseDto>> listar(Pageable pageable);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar histórico de estoque por ID", description = """
           Buscar histórico de estoque por ID
           ---
           Retorna os detalhes de um registro específico do histórico de movimentação de estoque com base no seu identificador.
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o histórico de estoque é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoEstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o histórico de estoque não é encontrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<Page<HistoricoEstoqueResponseDto>> buscarPorId(@PathVariable Integer id, Pageable pageable);
}