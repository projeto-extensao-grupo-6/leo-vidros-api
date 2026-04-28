package com.project.extension.controller.solicitacao;

import com.project.extension.controller.solicitacao.dto.SolicitacaoRequestDto;
import com.project.extension.controller.solicitacao.dto.SolicitacaoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<SolicitacaoResponseDto> cadastrarSolicitacao(@Valid @RequestBody SolicitacaoRequestDto dto);

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
            @ApiResponse(responseCode = "200", description = "Lista paginada de solicitações filtradas por nome (vazia se não houver correspondências)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResponseDto.class)
                    ))
    })
    ResponseEntity<Page<SolicitacaoResponseDto>> listarPorNome(
            @RequestParam(required = false)
            @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{2,50}$", message = "Nome deve conter apenas letras e espaços (2-50 caracteres)")
            String nome, Pageable pageable, Authentication authentication
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
                    description = "Lista paginada de solicitações filtradas por status (vazia se não houver correspondências)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResponseDto.class)
                    )
            )
    })
    ResponseEntity<Page<SolicitacaoResponseDto>> listar(@RequestParam(required = false) String status, Pageable pageable);

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
