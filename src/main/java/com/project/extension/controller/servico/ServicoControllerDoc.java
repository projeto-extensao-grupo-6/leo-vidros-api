package com.project.extension.controller.servico;

import com.project.extension.dto.servico.ServicoRequestDto;
import com.project.extension.dto.servico.ServicoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Serviços", description = "Operações relacionadas ao gerenciamento de serviços")
public interface ServicoControllerDoc {

    @PostMapping
    @Operation(summary = "Cadastrar um novo serviço", description = """
            Cadastrar uma nova serviços
            ---
            Cadastrar uma nova serviços no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ServicoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao validar o corpo da requisição")
    })
    ResponseEntity<ServicoResponseDto> cadastrar(@RequestBody ServicoRequestDto dto);

    @GetMapping
    @Operation(
            summary = "Listar serviços",
            description = """
            Lista todas as serviços cadastradas, com opção de filtrar por etapa via parâmetro.
            ---
            Exemplo: /servicoes?etapa=PENDENTE
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quando existem serviços com o status informado (ou todas, se o parâmetro não for informado)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicoResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Quando não existe nenhuma solicitação com o status informado"
            )
    })
    ResponseEntity<List<ServicoResponseDto>> listar(@RequestParam(required = false) String etapa);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID", description = """
            Buscar serviço por id
            ---
            Buscar serviços por id no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço encontrado",
                    content = @Content(schema = @Schema(implementation = ServicoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Integer id);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um serviço existente", description = """
           Atualizar produto
            ---
           Atualizar produto no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ServicoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    ResponseEntity<ServicoResponseDto> atualizar(@PathVariable Integer id,
                                                 @RequestBody ServicoRequestDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um serviço", description = """
             Deleta um produto no banco de dados com base no id fornecido.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Serviço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    ResponseEntity<String> deletar(@PathVariable Integer id);
}
