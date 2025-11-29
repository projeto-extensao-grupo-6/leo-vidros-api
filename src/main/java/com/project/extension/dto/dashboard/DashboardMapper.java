package com.project.extension.dto.dashboard;

import com.project.extension.entity.Agendamento;
import org.springframework.stereotype.Component;

import java.util.List;

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

//    public ProximosAgendamentosResponseDto toProximosAgendamentosResponseDto(List<Agendamento> agendamento){
//        return new ProximosAgendamentosResponseDto(agendamento);
//    }
}