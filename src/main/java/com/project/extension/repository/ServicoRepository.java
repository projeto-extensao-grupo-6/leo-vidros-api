package com.project.extension.repository;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico> findAllByEtapa(Etapa etapa);
}
