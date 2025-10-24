package com.project.extension.controller.estoque;

import com.project.extension.dto.estoque.EstoqueRequestDto;
import com.project.extension.dto.estoque.EstoqueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estoques", description = "Operações relacionadas ao gerenciamento de estoque de produtos")
public interface EstoqueControllerDoc {

    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada de produto", description = """
            Registrar entrada de produto
            ---
            Adiciona uma quantidade de determinado produto em uma localização específica do estoque.
            Caso o produto ainda não exista no local, será criado com a quantidade informada.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando a entrada é registrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o produto não é encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Quando a requisição está incorreta",
                    content = @Content())
    })
    ResponseEntity<EstoqueResponseDto> entrada(@Valid @RequestBody EstoqueRequestDto dto);

    @PostMapping("/saida")
    @Operation(summary = "Registrar saída de produto", description = """
            Registrar saída de produto
            ---
            Remove uma quantidade de determinado produto de uma localização específica do estoque.
            Caso a quantidade solicitada ultrapasse o disponível, o estoque é ajustado para zero.
            O produto não é removido, mesmo que a quantidade final seja zero.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando a saída é registrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o produto ou a localização não são encontrados",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Quando a requisição está incorreta",
                    content = @Content())
    })
    ResponseEntity<EstoqueResponseDto> saida(@Valid @RequestBody EstoqueRequestDto dto);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estoque por id", description = """
           Buscar estoque por id
           ---
           Retorna os dados de um estoque específico com base no seu identificador.
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o estoque é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o estoque não é encontrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<EstoqueResponseDto> buscarPorId(@PathVariable Integer id);

    @GetMapping
    @Operation(summary = "Listar todo o estoque", description = """
           Listar todos o estoque
           ---
           Retorna todos os registros de estoque cadastrados no banco de dados.
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando há registros de estoque no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstoqueResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum estoque cadastrado",
                    content = @Content())
    })
    ResponseEntity<List<EstoqueResponseDto>> listar();

}
