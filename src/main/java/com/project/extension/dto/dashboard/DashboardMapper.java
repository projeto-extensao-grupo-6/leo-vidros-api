package com.project.extension.dto.dashboard;

import com.project.extension.entity.Agendamento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardMapper {

    public ItensAbaixoMinimoKpiResponseDto toItensAbaixoMinimoDto(int quantidade) {
        return new ItensAbaixoMinimoKpiResponseDto(quantidade);
    }
    public EstoqueCriticoResponseDto toResponse(EstoqueCriticoResponseDto dto) {
        return new EstoqueCriticoResponseDto(
                dto.quantidadeTotal(),
                dto.quantidadeDisponivel(),
                dto.reservado(),
                dto.localizacao(),
                dto.nomeProduto(),
                dto.descricaoProduto(),
                dto.unidadeMedida(),
                dto.preco(),
                dto.nivelMinimo(),
                dto.nivelMaximo()
        );
    }

    public List<EstoqueCriticoResponseDto> toResponseListEstoqueCritico(List<EstoqueCriticoResponseDto> lista) {
        return lista.stream()
                .map(this::toResponse)
                .toList();
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