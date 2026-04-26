package com.project.extension.controller.pedido.servico;

import com.project.extension.controller.pedido.servico.dto.PedidoRequestDto;
import com.project.extension.controller.pedido.servico.dto.PedidoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<PedidoResponseDto> salvar(@Valid @RequestBody PedidoRequestDto request);

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
            @ApiResponse(responseCode = "200", description = "Lista paginada de pedidos (vazia se não houver registros)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<PedidoResponseDto>> buscarTodos(Pageable pageable);


    @GetMapping("findAllBy")
    @Operation(
            summary = "Buscar pedidos de serviço por etapa",
            description = """
            Retorna todos os pedidos filtrados pelo nome da etapa.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de pedidos filtrados por etapa (vazia se não houver correspondências)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<PedidoResponseDto>> buscarPorTipoAndEtapa(@RequestParam(required = true) String nome, Pageable pageable);

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
    ResponseEntity<PedidoResponseDto> atualizar(@Valid @RequestBody PedidoRequestDto request, @PathVariable Integer id);

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

    @GetMapping("/servicos")
    @Operation(
            summary = "Listar pedidos de serviço",
            description = """
        Retorna todos os pedidos cujo tipo é 'serviço'.
        ---
        Esse endpoint lista apenas pedidos associados a um serviço.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de pedidos de serviço (vazia se não houver registros)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<PedidoResponseDto>> buscarPedidosDeServico(Pageable pageable);


    @GetMapping("/produtos")
    @Operation(
            summary = "Listar pedidos de produto",
            description = """
        Retorna todos os pedidos cujo tipo é 'produto'.
        ---
        Esse endpoint lista apenas pedidos associados a itens de produto.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de pedidos de produto (vazia se não houver registros)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<PedidoResponseDto>> buscarPedidosDeProduto(Pageable pageable);
}
