package com.project.extension.repository;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Servico;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico> findAllByEtapa(Etapa etapa);

    @Query(value = "SELECT * FROM servico ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Servico findUltimoServico();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM servico WHERE pedido_id = :pedidoId", nativeQuery = true)
    void deleteByPedidoId(Integer pedidoId);
}
