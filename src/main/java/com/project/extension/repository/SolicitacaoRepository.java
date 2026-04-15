package com.project.extension.repository;

import com.project.extension.entity.Solicitacao;
import com.project.extension.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {
    List<Solicitacao> findByStatus(Status status);

    Page<Solicitacao> findAllByStatusNomeIgnoreCase(String status, Pageable pageable);

    Page<Solicitacao> findAllByNomeIgnoreCase(String nome, Pageable pageable);
}
