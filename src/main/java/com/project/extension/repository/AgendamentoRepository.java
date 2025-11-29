package com.project.extension.repository;

import com.project.extension.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query("SELECT COUNT(*) FROM agendamento a WHERE DATE(a.data_agendamento) = CURRENT_TIMESTAMP")
    int countQtdAgendamentosHoje();

    @Query("SELECT COUNT(*) FROM agendamento a WHERE DATE(a.data_agendamento) > CURRENT_TIMESTAMP")
    int countQtdAgendamentosFuturos();
}
