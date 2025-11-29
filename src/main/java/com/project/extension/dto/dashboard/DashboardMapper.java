package com.project.extension.dto.dashboard;

import org.springframework.stereotype.Component;

@Component
public class DashboardMapper {

    public ItensAbaixoMinimoKpiResponseDto toItensAbaixoMinimoDto(int quantidade) {
        return new ItensAbaixoMinimoKpiResponseDto(quantidade);
    }

    public QtdAgendamentosHojeResponseDto toAgendamentosHojeDto(int qtdAgendamentosHoje){
        return new QtdAgendamentosHojeResponseDto(qtdAgendamentosHoje);
    }

    public QtdAgendamentosFuturosResponseDto toAgendamentosFuturosDto(int qtdAgendamentosFuturos){
        return new QtdAgendamentosFuturosResponseDto(qtdAgendamentosFuturos);
    }
}