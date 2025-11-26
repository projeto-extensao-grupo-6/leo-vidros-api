package com.project.extension.repository;

import com.project.extension.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    List<ItemPedido> findAllByPedidoId(Integer pedidoId);
}
