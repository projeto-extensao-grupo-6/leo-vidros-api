package com.project.extension.controller.solicitacao;

import com.project.extension.dto.solicitacao.SolicitacaoRequestDto;
import com.project.extension.dto.solicitacao.SolicitacaoResponseDto;
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

@Tag(name = "Solicitações", description = "Operações relacionadas às solicitações de novos usuários")
public interface SolicitacaoControllerDoc {

    @PostMapping
    @Operation(summary = "Cadastrar solicitação", description = """
            Cadastrar uma nova solicitação
            ---
            Cadastrar uma nova solicitação no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação cadastrada com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SolicitacaoResponseDto.class)
                )),
            @ApiResponse(responseCode = "400", description = "Erro no corpo da requisição")
    })
    ResponseEntity<SolicitacaoResponseDto> cadastrarSolicitacao(@RequestBody SolicitacaoRequestDto dto);

    @GetMapping("findAllBy")
    @Operation(
            summary = "Buscar solicitações por nome",
            description = """
                Lista todas as solicitações cadastradas, com opção de filtrar por nome via parâmetro.
                ---
                Se o parâmetro 'nome' for informado, filtra pelo nome do solicitante (busca parcial).
                Exemplo:
                /solicitacao?nome=Maria
                """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitações encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResponseDto.class)
                    )),
            @ApiResponse(responseCode = "204", description = "Nenhuma solicitação encontrada")
    })
    ResponseEntity<List<SolicitacaoResponseDto>> listarPorNome(
            @RequestParam(required = false) String nome, Authentication authentication
    );

    @GetMapping("")
    @Operation(
            summary = "Listar solicitações",
            description = """
            Lista todas as solicitações cadastradas, com opção de filtrar por status via parâmetro.
            ---
            Exemplo: /solicitacoes?status=PENDENTE
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quando existem solicitações com o status informado (ou todas, se o parâmetro não for informado)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Quando não existe nenhuma solicitação com o status informado"
            )
    })
    ResponseEntity<List<SolicitacaoResponseDto>> listar(@RequestParam(required = false) String status);

    @PutMapping("/aceitar/{id}")
    @Operation(summary = "Aceitar solicitação", description = """
            Aceitar Solicitação de cadastro de novo usuário
            ---
            Aceitar Solicitação de cadastro de novo usuário no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando a solicitação foi aceita com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SolicitacaoResponseDto.class)
                )),
            @ApiResponse(responseCode = "400", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<Void> aceitarSolicitacao(@PathVariable Integer id);

    @DeleteMapping("/recusar/{id}")
    @Operation(summary = "Recusar solicitação", description = """
            Recusar solicitação de novo usuário
            ---
            Recusar solicitação de novo usuário no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando a solicitação é recusada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido",
                    content = @Content())
    })
    ResponseEntity<Void> recusarSolicitacao(@PathVariable Integer id);
}
