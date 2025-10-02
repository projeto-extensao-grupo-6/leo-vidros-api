package com.project.extension.repository;

import com.project.extension.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico> findAllByData(LocalDate data);

    @Query("SELECT s FROM Servico s WHERE MONTH(s.data) = :mes")
    List<Servico> findAllByMes(@Param("mes") int mes);

}
