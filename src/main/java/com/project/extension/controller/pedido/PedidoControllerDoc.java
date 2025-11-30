package com.project.extension.controller.pedido;

import com.project.extension.dto.itemproduto.PedidoProdutoRequestDto;
import com.project.extension.dto.itemproduto.PedidoProdutoResponseDto;
import com.project.extension.dto.pedido.PedidoRequestDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento de pedidos de clientes internos e externos")
public interface PedidoControllerDoc {

    @PostMapping()
    @Operation(summary = "Salvar pedido", description = """
            Salvar pedido
            ---
            Salva pedido no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o pedido é cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<PedidoResponseDto> salvar(@RequestBody PedidoRequestDto request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por id", description = """
           Buscar pedido por id
            ---
           Buscar pedido por id no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o pedido é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o pedido não for encontrado pelo id no banco de dados",
                    content = @Content())
    })
    ResponseEntity<PedidoResponseDto> buscarPorId(@PathVariable Integer id);


    @GetMapping()
    @Operation(summary = "Buscar todos os pedido", description = """
           Buscar todos os pedido
            ---
           Buscar todos os pedido que estão cadastrados no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando existe pedido cadastrado no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum pedido cadastrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<List<PedidoResponseDto>> buscarTodos();

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = """
           Atualizar pedido
            ---
           Atualizar pedido no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando pedido foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<PedidoResponseDto> atualizar(@RequestBody PedidoRequestDto request, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido por id", description = """
        Deleta um pedido no banco de dados com base no id fornecido.
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

    // NOVO MÉTODO: Criação de Pedido de Produto
    @PostMapping("/produtos")
    @Operation(summary = "Criar novo pedido de produto (com baixa de estoque)", description = """
            Cria um pedido e, de forma transacional, valida a disponibilidade e dá baixa no estoque.
            ---
            Se o estoque for insuficiente para qualquer item, a transação é revertida (rollback).
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o pedido de produto é criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoProdutoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o estoque for insuficiente ou a requisição for inválida",
                    content = @Content())
    })
    ResponseEntity<PedidoProdutoResponseDto> criarPedidoProduto(@RequestBody PedidoProdutoRequestDto request);
}
