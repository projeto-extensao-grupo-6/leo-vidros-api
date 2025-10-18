package com.project.extension.controller.auth;

import com.project.extension.dto.auth.AuthRequestDto;
import com.project.extension.dto.auth.AuthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Authentication", description = "Operações relacionadas a autenticação de usuários")
public interface AuthControllerDoc {

    @PostMapping("/login")
    @Operation(summary = "Autentificação de usuário", description = """
            Autentificação de usuário
            ---
            Autentificação de usuário no banco de dados
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quando o usuário é autenticado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Quando o corpo de requisição está incorreto",
                    content = @Content())
    })
    ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request);
}
