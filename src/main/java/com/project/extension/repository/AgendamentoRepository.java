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

//    @Query(
//            value = """
//        SELECT
//            a.id AS agendamentoId,
//            a.data_agendamento AS dataAgendamento,
//            a.observacao AS agendamentoObservacao,
//
//            p.id AS pedidoId,
//            p.valor_total AS pedidoValorTotal,
//            p.observacao AS pedidoObservacao,
//            p.ativo AS pedidoAtivo,
//
//            e.id AS enderecoId,
//            e.logradouro AS logradouro,
//            e.numero AS numero,
//            e.complemento AS complemento,
//            e.bairro AS bairro,
//            e.cidade AS cidade,
//            e.estado AS estado,
//            e.uf AS uf,
//            e.cep AS cep,
//            e.referencia AS referencia,
//            e.tipo AS enderecoTipo,
//
//            s.id AS statusId,
//            s.nome AS statusNome
//
//        FROM agendamento a
//        JOIN pedido p ON a.pedido_id = p.id
//        JOIN endereco e ON a.endereco_id = e.id
//        JOIN status s ON a.status_id = s.id
//        WHERE a.data_agendamento >= NOW()
//        ORDER BY a.data_agendamento ASC
//        """,
//            nativeQuery = true
//    )
//    List<ProximosAgendamentosResponseDto> proximosAgendamentos();

}
