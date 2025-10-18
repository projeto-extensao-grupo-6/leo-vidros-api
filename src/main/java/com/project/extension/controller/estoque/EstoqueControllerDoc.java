package com.project.extension.controller.estoque;

import com.project.extension.entity.Estoque;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estoque", description = "Operações relacionadas ao controle de estoque")
public interface EstoqueControllerDoc {

    // POST /estoque
    @PostMapping
    @Operation(summary = "Salvar item de estoque", description = "Salvar um novo item no estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item de estoque cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Estoque.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Corpo de requisição inválido ou dados incorretos",
                    content = @Content())
    })
    Estoque salvarProdutosEstoque(@RequestBody Estoque request);


    // GET /estoque/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Buscar item de estoque por ID", description = "Buscar item de estoque específico no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de estoque encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Estoque.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<Estoque> buscarProdutoEstoquePorId(@PathVariable Long id);


    // GET /estoque
    @GetMapping
    @Operation(summary = "Listar todos os produtos em estoque", description = "Buscar todos os itens de estoque cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Estoque.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Nenhum item de estoque cadastrado",
                    content = @Content())
    })
    List<Estoque> listarProdutosEstoque();


    // PUT /estoque/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de estoque", description = "Atualizar as informações de um item de estoque existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de estoque atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Estoque.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Corpo de requisição inválido",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<Estoque> atualizarProdutosEstoque(@PathVariable Long id, @RequestBody Estoque request);


    // DELETE /estoque/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item de estoque por ID", description = "Remove um item de estoque com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item de estoque deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<Void> deletarProdutoEstoque(@PathVariable Long id);
}