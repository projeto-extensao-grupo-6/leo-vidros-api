package com.project.extension.controller.dashboard;


import com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto;
import com.project.extension.dto.dashboard.QtdAgendamentosFuturosResponseDto;
import com.project.extension.dto.dashboard.QtdAgendamentosHojeResponseDto;
import com.project.extension.dto.dashboard.ItensAbaixoMinimoKpiResponseDto;
import com.project.extension.dto.produto.ProdutoResponseDto;
import com.project.extension.entity.Agendamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dashboard", description = "Getters de KPI ou informações utilizadas no Painel de Controle/Dashboard")
public interface DashboardControllerDoc {

    @GetMapping("/itens-abaixo-minimo")
    @Operation(
            summary = "Quantidade de itens abaixo do nível mínimo",
            description = "Retorna o total de itens no estoque cuja quantidade disponível está abaixo do nível mínimo definido na métrica de estoque."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "integer",
                                    example = "7",
                                    description = "Número total de itens abaixo do mínimo"
                            )
                    )
            )
    })
    ResponseEntity<ItensAbaixoMinimoKpiResponseDto> getItensAbaixoMinimo();

    @GetMapping("/qtd-agendamentos-hoje")
    @Operation(summary = "Buscar quantidade de agendamentos hoje", description = """
               Busca na quantidade de agendamentos no dia de hoje
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Puxa qualquer valor do banco, se a tabela estiver sem registros no dia ou vazia retorna 0""",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
    })
    ResponseEntity<QtdAgendamentosHojeResponseDto> getQtdAgendamentosHoje();

    @GetMapping("/qtd-agendamentos-futuros")
    @Operation(summary = "Buscar quantidade de agendamentos futuros", description = """
               Busca na quantidade de agendamentos no dia de futuros
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Puxa qualquer valor do banco, se a tabela estiver sem registros futuros ou vazia retorna 0""",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDto.class)
                    )),
    })
    ResponseEntity<QtdAgendamentosFuturosResponseDto> getQtdAgendamentosFuturos();
//
//    @GetMapping("/agendamentos-futuros")
//    ResponseEntity<ProximosAgendamentosResponseDto> proximosAgendamentos();
}
