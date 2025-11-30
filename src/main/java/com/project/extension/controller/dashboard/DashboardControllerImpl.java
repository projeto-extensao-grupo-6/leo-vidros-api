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
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvLmZiZXplcnJhQHNwdGVjaC5zY2hvb2wiLCJpYXQiOjE3NjQ1MzMxMzksImV4cCI6MTc2NDYxOTUzOX0.irYL6r8VbMQSFcVGff_X_5afYEnaODgoByVqCguJjwg
    @Override
    public ResponseEntity<List<ProximosAgendamentosResponseDto>> proximosAgendamentos() {
        var dto = dashboardService.proximosAgendamentos();
        return ResponseEntity.ok(mapper.toProximosAgendamentosResponseDto(dto));
    }
}
