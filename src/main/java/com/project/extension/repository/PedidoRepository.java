package com.project.extension.repository;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findAllByEtapa(Etapa etapa);

    List<Pedido> findByEtapaTipoAndEtapaNome(String tipoEtapa, String nomeEtapa);
}
