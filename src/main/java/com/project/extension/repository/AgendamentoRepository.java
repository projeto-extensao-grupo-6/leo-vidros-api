package com.project.extension.repository;

import com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto;
import com.project.extension.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query(
            value = """   
                SELECT COUNT(*)
                FROM agendamento a
                WHERE DATE(a.data_agendamento) = CURRENT_DATE
            """,
                 nativeQuery = true
          )
    int countQtdAgendamentosHoje();

    @Query(
            value = """
                SELECT COUNT(*)
                FROM agendamento a
                WHERE DATE(a.data_agendamento) = CURRENT_DATE
                AND a.tipo = "SERVICO";
                """,
            nativeQuery = true
    )
    int countServicosHoje();

    @Query(
            value = """
                SELECT COUNT(*)
                FROM agendamento a
                WHERE DATE(a.data_agendamento) > CURRENT_TIMESTAMP
        """,
            nativeQuery = true
    )
    int countQtdAgendamentosFuturos();

    @Query("""
        SELECT new com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto(
               a.id,
               a.dataAgendamento,
               a.inicioAgendamento,
               a.fimAgendamento,
               a.observacao,
               CAST(s.precoBase AS java.math.BigDecimal),
               s.pedido.observacao,
               s.ativo,
               st.nome
        )
        FROM Agendamento a
        JOIN a.servico s
        LEFT JOIN s.pedido p
        JOIN a.statusAgendamento st
        WHERE a.dataAgendamento = CURRENT_DATE
        ORDER BY a.inicioAgendamento ASC
    """)
    List<ProximosAgendamentosResponseDto> proximosAgendamentos();



    @Query(value = """
        SELECT
            CAST(
                (SUM(TIMESTAMPDIFF(MINUTE, a.inicio_agendamento, a.fim_agendamento)) / 60) /
                (COUNT(DISTINCT af.funcionario_id) * 9) * 100
            AS DECIMAL(5,2)) AS taxa_ocupacao_percentual
                FROM agendamento a
                JOIN agendamento_funcionario af ON a.id = af.agendamento_id
                WHERE a.data_agendamento = CURRENT_DATE
                GROUP BY a.data_agendamento;
        """,
    nativeQuery = true)

    Double taxaOcupacaoServicos();

    @Query("""
        SELECT a FROM Agendamento a
        JOIN a.funcionarios f
        WHERE f.id = :funcionarioId
        AND a.dataAgendamento BETWEEN :dataInicio AND :dataFim
        ORDER BY a.dataAgendamento ASC, a.inicioAgendamento ASC
    """)
    List<Agendamento> findAgendamentosByFuncionarioAndPeriodo(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("""
        SELECT a FROM Agendamento a
        JOIN a.funcionarios f
        WHERE f.id = :funcionarioId
        AND a.dataAgendamento = :data
        AND (:novoInicio < a.fimAgendamento AND :novoFim > a.inicioAgendamento)
    """)
    List<Agendamento> findConflitos(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("data") LocalDate data,
            @Param("novoInicio") LocalTime novoInicio,
            @Param("novoFim") LocalTime novoFim
    );

    @Query("""
        SELECT COUNT(f) FROM Agendamento a
        JOIN a.funcionarios f
        WHERE a.id = :agendamentoId
    """)
    int countFuncionariosByAgendamentoId(@Param("agendamentoId") Integer agendamentoId);

}
