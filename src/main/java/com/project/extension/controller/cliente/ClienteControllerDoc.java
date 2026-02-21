package com.project.extension.controller.cliente;

import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.dto.cliente.ClienteRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Operações relacionadas ao gerenciamento de clientes")
public interface ClienteControllerDoc {

    @PostMapping()
    @Operation(summary = "Salvar cliente", description = """
            Salvar cliente
            ---
            Salva cliente no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o cliente é cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<ClienteResponseDto> salvar(@RequestBody ClienteRequestDto request, Authentication authentication);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por id", description = """
           Buscar cliente por id
            ---
           Buscar cliente por id no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o cliente é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o cliente não for encontrado pelo id no banco de dados",
                    content = @Content())
    })
    ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id, Authentication authentication);


    @GetMapping()
    @Operation(summary = "Buscar todos os cliente", description = """
           Buscar todos os cliente
            ---
           Buscar todos os cliente que estão cadastrados no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando existe cliente cadastrado no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum cliente cadastrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<List<ClienteResponseDto>> buscarTodos(Authentication authentication);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = """
           Atualizar cliente
            ---
           Atualizar cliente no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando cliente foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Quando o cliente do ID não existe no banco de dados",
                    content = @Content())
    })
    ResponseEntity<ClienteResponseDto> atualizar(@RequestBody ClienteRequestDto request, @PathVariable Integer id, Authentication authentication);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente por id", description = """
        Deleta um cliente no banco de dados com base no id fornecido.
        """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<String> deletar(@PathVariable Integer id, Authentication authentication);
}
