package com.project.extension.service;

import com.project.extension.dto.dashboard.EstoqueCriticoResponseDto;
import com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardService {

    private EstoqueRepository estoqueRepository;
    private AgendamentoRepository agendamentoRepository;

    public int getItensAbaixoMinimo() {
        return estoqueRepository.countItensAbaixoMinimo();
    }

    public int qtdServicosHoje(){
        return agendamentoRepository.countServicosHoje();
    }

    public List<EstoqueCriticoResponseDto> estoqueCritico() {

        List<Object[]> raw = estoqueRepository.estoqueCriticoRaw();

        return raw.stream()
                .map(r -> new EstoqueCriticoResponseDto(
                        (Integer) r[0] ,
                        toBigDecimal(r[1]),
                        toBigDecimal(r[2]),
                        toBigDecimal(r[3]),
                        (String) r[4],
                        (String) r[5],
                        (String) r[6],
                        (String) r[7],
                        toBigDecimal(r[8]),
                        (Integer) r[9],
                        (Integer) r[10]
                )).toList();
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal bd) return bd;
        if (value instanceof Number n) return new BigDecimal(n.toString());
        throw new IllegalArgumentException("Valor não numérico: " + value);
    }

    public int getQtdAgendamentosHoje(){
        return agendamentoRepository.countQtdAgendamentosHoje();
    }

    public int getQtdAgendamentosFuturos(){
        return agendamentoRepository.countQtdAgendamentosFuturos();
    }

    public Double taxaOcupacaoServicos(){
        return agendamentoRepository.taxaOcupacaoServicos();
    }

    public List<ProximosAgendamentosResponseDto> proximosAgendamentos() {
        return agendamentoRepository.proximosAgendamentos();
    }
}
