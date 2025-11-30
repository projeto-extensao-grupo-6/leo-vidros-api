package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "forma_pagamento")
    private String formaPagamento;
    private TipoPedido tipoPedido;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itensPedido = new ArrayList<>();

    public Pedido(BigDecimal valorTotal, Boolean ativo, String observacao, String formaPagamento, TipoPedido tipoPedido) {
        this.valorTotal = valorTotal;
        this.ativo = ativo;
        this.observacao = observacao;
        this.formaPagamento = formaPagamento;
        this.tipoPedido = tipoPedido;
    }
}
