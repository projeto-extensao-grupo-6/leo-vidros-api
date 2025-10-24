package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    private Boolean ativo;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    private Etapa etapa;

    public Pedido(BigDecimal valorTotal, Boolean ativo, String observacao) {
        this.valorTotal = valorTotal;
        this.ativo = ativo;
        this.observacao = observacao;
    }
}
