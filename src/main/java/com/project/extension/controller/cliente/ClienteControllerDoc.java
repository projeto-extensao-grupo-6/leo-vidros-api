package com.project.extension.controller.cliente;

import com.project.extension.dto.cliente.ClienteRequestDto;
import com.project.extension.dto.cliente.ClienteResponseDto;
import com.project.extension.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")

public interface ClienteControllerDoc {


    @PostMapping
    @Operation(summary = "Cadastrar Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o cliente é cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Quando o corpo da requisição está incorreto",
                    content = @Content())
    })
     ResponseEntity<ClienteResponseDto> cadastrar(@RequestBody ClienteRequestDto request) ;

    @GetMapping
    @Operation(summary = "Listar Clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "Quando não há clientes cadastrados",
                    content = @Content())
    })
     ResponseEntity<List<ClienteResponseDto>> listar() ;

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o cliente é atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Quando o corpo da requisição está incorreto",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Quando o cliente não é encontrado",
                    content = @Content())
    })
    ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Integer id, @RequestBody ClienteRequestDto request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Quando o cliente é deletado com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Quando o cliente não é encontrado",
                    content = @Content())
    })
    ResponseEntity<Void> deletar(@PathVariable Integer id) ;
}
