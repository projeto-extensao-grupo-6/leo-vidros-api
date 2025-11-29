package com.project.extension.service;

import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardService {

    private EstoqueRepository estoqueRepository;
    private AgendamentoRepository agendamentoRepository;

    public int getItensAbaixoMinimo() {
        return estoqueRepository.countItensAbaixoMinimo();
    }

    public int getQtdAgendamentosHoje(){
        return agendamentoRepository.countQtdAgendamentosHoje();
    }

    public int getQtdAgendamentosFuturos(){
        return agendamentoRepository.countQtdAgendamentosFuturos();
    }

}
