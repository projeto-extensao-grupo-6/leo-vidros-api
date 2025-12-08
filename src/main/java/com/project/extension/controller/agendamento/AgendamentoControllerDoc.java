package com.project.extension.controller.agendamento;

import com.project.extension.dto.agendamento.AgendamentoRequestDto;
import com.project.extension.dto.agendamento.AgendamentoResponseDto;
import com.project.extension.dto.pedido.servico.agendamento.AgendamentoServicoRequestDto;
import com.project.extension.dto.pedido.servico.agendamento.AgendamentoServicoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamento de serviço e orçamento")
public interface AgendamentoControllerDoc {

    @PostMapping()
    @Operation(summary = "Salvar agendamento", description = """
            Salvar agendamento
            ---
            Salva agendamento no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o agendamento é cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<AgendamentoResponseDto> salvar(@RequestBody AgendamentoRequestDto request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por id", description = """
           Buscar agendamento por id
            ---
           Buscar agendamento por id no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o agendamento é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o agendamento não for encontrado pelo id no banco de dados",
                    content = @Content())
    })
    ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Integer id);


    @GetMapping()
    @Operation(summary = "Buscar todos os agendamento", description = """
           Buscar todos os agendamento
            ---
           Buscar todos os agendamento que estão cadastrados no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando existe agendamento cadastrado no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum usuaŕio cadastrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<List<AgendamentoResponseDto>> buscarTodos();

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento", description = """
           Atualizar agendamento
            ---
           Atualizar agendamento no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando agendamento foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<AgendamentoResponseDto> atualizar(@RequestBody AgendamentoRequestDto request, @PathVariable Integer id);

    @PutMapping("/dados-basicos/{id}")
    @Operation(summary = "Atualizar dados básicos do agendamento", description = """
           Atualizar dados básicos do agendamento
            ---
           Atualizar dados básicos do agendamento no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando agendamento foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoServicoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<AgendamentoServicoResponseDto> atualizarDadosBasicos(@RequestBody AgendamentoServicoRequestDto request, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar agendamento por id", description = """
        Deleta um agendamento no banco de dados com base no id fornecido.
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