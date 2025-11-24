package com.project.extension.controller.historicoestoque;

import com.project.extension.dto.historicoestoque.HistoricoEstoqueResponseDto;
import com.project.extension.entity.HistoricoEstoque;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Históricos de Estoques", description = "Operações relacionadas à visualização de histórico de movimentações de estoque")
public interface HistoricoEstoqueControllerDoc {

    @GetMapping
    @Operation(summary = "Listar históricos de estoque", description = """
           Listar históricos de estoque
           ---
           Retorna todos os registros de movimentações realizadas no estoque, incluindo entradas e saídas de produtos.
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando há registros de histórico de estoque no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoEstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum histórico de estoque cadastrado",
                    content = @Content())
    })
    ResponseEntity<List<HistoricoEstoqueResponseDto>> listar();

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
    ResponseEntity<List<HistoricoEstoqueResponseDto>> buscarPorId(@PathVariable Integer id);
}