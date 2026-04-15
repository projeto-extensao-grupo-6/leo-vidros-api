package com.project.extension.repository;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Page<Pedido> findAllByServico_Etapa(Etapa etapa, Pageable pageable);

    Page<Pedido> findByTipoPedidoIgnoreCase(String tipo, Pageable pageable);
}
