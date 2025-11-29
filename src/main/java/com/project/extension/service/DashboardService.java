package com.project.extension.service;

import com.project.extension.dto.dashboard.EstoqueCriticoResponseDto;
import com.project.extension.entity.Agendamento;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardService {
    //TODO Mudar nomes dos metodos, retirar o get e colocar algo como "listar"
    private EstoqueRepository estoqueRepository;
    private AgendamentoRepository agendamentoRepository;

    public int getItensAbaixoMinimo() {
        return estoqueRepository.countItensAbaixoMinimo();
    }

    public List<EstoqueCriticoResponseDto> estoqueCritico(){
        return estoqueRepository.estoqueCritco();
    }

    public int getQtdAgendamentosHoje(){
        return agendamentoRepository.countQtdAgendamentosHoje();
    }

    public int getQtdAgendamentosFuturos(){
        return agendamentoRepository.countQtdAgendamentosFuturos();
    }


//    public List<ProximosAgendamentosResponseDto> proximosAgendamentos(){
//        List<Agendamento> agendamentos = agendamentoRepository.proximosAgendamentos();
//        return agendamentos;
//

}
