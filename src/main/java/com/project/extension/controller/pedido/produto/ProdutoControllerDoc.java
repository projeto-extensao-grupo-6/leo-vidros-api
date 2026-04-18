package com.project.extension.controller.pedido.produto;


import com.project.extension.controller.pedido.produto.dto.ProdutoRequestDto;
import com.project.extension.controller.pedido.produto.dto.ProdutoResponseDto;
import com.project.extension.controller.pedido.produto.dto.ProdutoStatusRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Produtos", description = "Operações relacionadas ao gerenciamento de produtos")
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
            @ApiResponse(responseCode = "200", description = "Lista paginada de produtos (vazia se não houver registros)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<ProdutoResponseDto>> buscarTodos(Pageable pageable);

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

    @PutMapping("/stand-by/{id}")
    @Operation(
            summary = "Colocar produto em stand-by",
            description = """
                Coloca o produt em estado de stand-by.
                ---
                Atualiza o status do produto para indicar que está temporariamente indisponível.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto colocado em stand-by com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque ou produto não encontrado",
                    content = @Content()
            )
    })
    ResponseEntity<ProdutoResponseDto> standBy(@PathVariable Integer id, @RequestBody ProdutoStatusRequestDto dto);
}
