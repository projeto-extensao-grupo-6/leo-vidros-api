package com.project.extension.controller.dashboard;

import com.project.extension.dto.dashboard.*;
import com.project.extension.entity.Agendamento;
import com.project.extension.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardControllerImpl implements DashboardControllerDoc {

    private final DashboardService dashboardService;
    private final DashboardMapper mapper;

    @Override
    public ResponseEntity<ItensAbaixoMinimoKpiResponseDto> getItensAbaixoMinimo() {
        var dto = dashboardService.getItensAbaixoMinimo();
        return ResponseEntity.ok(mapper.toItensAbaixoMinimoDto(dto));
    }

    @Override
    public ResponseEntity<List<EstoqueCriticoResponseDto>> listarEstoqueCritico() {
        var dto = dashboardService.estoqueCritico();
        return ResponseEntity.ok(mapper.toResponseListEstoqueCritico(dto));
    }

    @Override
    public ResponseEntity<QtdAgendamentosHojeResponseDto> getQtdAgendamentosHoje() {
        var dto = dashboardService.getQtdAgendamentosHoje();
        return ResponseEntity.ok(mapper.toAgendamentosHojeDto(dto));
    }

    @Override
    public ResponseEntity<QtdAgendamentosFuturosResponseDto> getQtdAgendamentosFuturos() {
        var dto = dashboardService.getQtdAgendamentosFuturos();
        return ResponseEntity.ok(mapper.toAgendamentosFuturosDto(dto));
    }
    @Override
    public ResponseEntity<List<ProximosAgendamentosResponseDto>> proximosAgendamentos() {
        var dto = dashboardService.proximosAgendamentos();
        return ResponseEntity.ok(mapper.toProximosAgendamentosResponseDto(dto));
    }

    @Override
    public ResponseEntity<TaxaOcupacaoServicosResponseDto> taxaOcupacaoServicos() {
        var dto = dashboardService.taxaOcupacaoServicos();
        return ResponseEntity.ok(mapper.toTaxaOcupacaoServicosResponseDto(dto));
    }

    @Override
    public ResponseEntity<QtdServicosHojeResponseDto> qtdServicosHoje() {
        var dto = dashboardService.qtdServicosHoje();
        return ResponseEntity.ok(mapper.toQtdServicosHojeResponseDto(dto));
    }
}
