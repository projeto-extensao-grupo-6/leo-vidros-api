package com.project.extension.controller.funcionario;

import com.project.extension.dto.funcionario.AgendaFuncionarioResponseDto;
import com.project.extension.dto.funcionario.FuncionarioDisponivelResponseDto;
import com.project.extension.dto.funcionario.FuncionarioRequestDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Tag(name = "Funcionários", description = "Operações relacionadas com gerenciamento de funcionários")
public interface FuncionarioControllerDoc {

    @PostMapping()
    @Operation(summary = "Salvar Funcionário", description = """
            Salvar Funcionário
            ---
            Salva Funcionário no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o Funcionário é cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<FuncionarioResponseDto> salvar(@RequestBody FuncionarioRequestDto request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Funcionário por id", description = """
           Buscar Funcionário por id
            ---
           Buscar Funcionário por id no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando o Funcionário é encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Quando o Funcionário não for encontrado pelo id no banco de dados",
                    content = @Content())
    })
    ResponseEntity<FuncionarioResponseDto> buscarPorId(@PathVariable Integer id);


    @GetMapping()
    @Operation(summary = "Buscar todos os Funcionário", description = """
           Buscar todos os Funcionário
            ---
           Buscar todos os Funcionário que estão cadastrados no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando existe Funcionário cadastrado no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Quando não há nenhum Funcionário cadastrado no banco de dados",
                    content = @Content())
    })
    ResponseEntity<List<FuncionarioResponseDto>> buscarTodos();

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Funcionário", description = """
           Atualizar Funcionário
            ---
           Atualizar Funcionário no banco de dados
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando Funcionário foi atualizado com sucesso no banco de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Quando o nome do role recebido no corpo não existe no banco de dados",
                    content = @Content())
    })
    ResponseEntity<FuncionarioResponseDto> atualizar(@RequestBody FuncionarioRequestDto request, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Funcionário por id", description = """
        Deleta um Funcionário no banco de dados com base no id fornecido.
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
    ResponseEntity<String> deletar(@PathVariable Integer id);

    @GetMapping("/{id}/agenda")
    @Operation(summary = "Buscar agenda do funcionário", description = """
            Buscar todos os agendamentos vinculados a um funcionário específico,
            filtrados por período (data início e data fim).
            Retorna informações do agendamento, serviço, cliente e etapa.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendaFuncionarioResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum agendamento encontrado no período",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                    content = @Content())
    })
    ResponseEntity<List<AgendaFuncionarioResponseDto>> buscarAgenda(
            @PathVariable Integer id,
            @Parameter(description = "Data de início do período (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data de fim do período (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    );

    @GetMapping("/disponiveis")
    @Operation(summary = "Buscar funcionários disponíveis", description = """
            Retorna apenas funcionários ativos que NÃO possuem conflito
            de horário na data e horário informados.
            Utilizado ao criar/editar agendamentos do tipo SERVICO.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de funcionários disponíveis",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioDisponivelResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum funcionário disponível",
                    content = @Content())
    })
    ResponseEntity<List<FuncionarioDisponivelResponseDto>> buscarDisponiveis(
            @Parameter(description = "Data do agendamento (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @Parameter(description = "Horário de início (HH:MM)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime inicio,
            @Parameter(description = "Horário de fim (HH:MM)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fim
    );
}
