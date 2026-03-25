package com.project.extension.repository;

import com.project.extension.entity.OrcamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoItemRepository extends JpaRepository<OrcamentoItem, Integer> {

    List<OrcamentoItem> findByOrcamentoIdOrderByOrdemAsc(Integer orcamentoId);
}
