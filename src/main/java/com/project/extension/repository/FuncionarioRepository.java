package com.project.extension.repository;

import com.project.extension.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    Funcionario findByTelefone(String telefone);

    List<Funcionario> findAllByAtivoTrue();

    @Query("""
        SELECT f FROM Funcionario f
        WHERE f.ativo = true
        AND NOT EXISTS (
            SELECT 1 FROM Agendamento a
            JOIN a.funcionarios af
            WHERE af.id = f.id
            AND a.dataAgendamento = :data
            AND (:novoInicio < a.fimAgendamento AND :novoFim > a.inicioAgendamento)
        )
    """)
    List<Funcionario> findDisponiveis(
            @Param("data") LocalDate data,
            @Param("novoInicio") LocalTime novoInicio,
            @Param("novoFim") LocalTime novoFim
    );
}
