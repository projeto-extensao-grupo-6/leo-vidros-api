package com.project.extension.repository;

import com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto;
import com.project.extension.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
                WHERE DATE(a.data_agendamento) > CURRENT_TIMESTAMP
        """,
            nativeQuery = true
    )
    int countQtdAgendamentosFuturos();

// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvLmZiZXplcnJhQHNwdGVjaC5zY2hvb2wiLCJpYXQiOjE3NjQ0NDA1MTMsImV4cCI6MTc2NDUyNjkxM30.He0ryPbqg3jMQIa-foRNfkrLPAkOLaeUSov4EicYb8s

    @Query("""
    SELECT new com.project.extension.dto.dashboard.ProximosAgendamentosResponseDto(
                  a.id AS idAgendamento,
                               a.dataAgendamento AS dataAgendamento,
                               a.inicioAgendamento AS inicioAgendamento,
                               a.fimAgendamento AS fimAgendamento,
                               a.observacao AS agendamentoObservacao,
                               p.valorTotal AS valorTotal,
                               p.observacao AS pedidoObservacao,
                               p.ativo AS ativo,
                               e.numero AS numero,
                               e.complemento AS complemento,
                               e.bairro AS bairro,
                               e.cidade AS cidade,
                               e.uf AS uf,
                               e.cep AS cep,
                               s.nome AS status
    )
    FROM Agendamento a
    JOIN a.pedido p
    JOIN a.endereco e
    JOIN a.statusAgendamento s
    WHERE a.dataAgendamento >= CURRENT_DATE
    ORDER BY a.dataAgendamento ASC
""")
    List<ProximosAgendamentosResponseDto> proximosAgendamentos();



}
