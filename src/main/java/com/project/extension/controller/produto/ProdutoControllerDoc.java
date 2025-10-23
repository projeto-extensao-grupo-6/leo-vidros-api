package com.project.extension.controller.produto;


import com.project.extension.dto.produto.ProdutoRequestDto;
import com.project.extension.dto.produto.ProdutoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "Operações relacionadas a criação, atualização, deleção e visualização de produto e atríbutos")
public interface ProdutoControllerDoc {

    @PostMapping
    @Operation(summary = "Salvar produto", description = """
            Salvar produto
            ---
            Salva produto no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o produto é cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<ProdutoResponseDto> salvar(@RequestBody ProdutoRequestDto request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por id", description = """
           Buscar produto por id
            ---
           Buscar produto por id no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o produto é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o produto não for encontrado pelo id no banco de dados",
                    content = @Content())
    })
    ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id);


    @GetMapping()
    @Operation(summary = "Buscar todos os produto", description = """
           Buscar todos os produto
            ---
           Buscar todos os produto que estão cadastrados no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando existe produto cadastrado no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum produto cadastrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<List<ProdutoResponseDto>> buscarTodos();

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = """
           Atualizar produto
            ---
           Atualizar produto no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando produto foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<ProdutoResponseDto> atualizar(@RequestBody ProdutoRequestDto request, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto por id", description = """
        Deleta um produto no banco de dados com base no id fornecido.
        """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido deletado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<String> deletar(@PathVariable Integer id);
}
