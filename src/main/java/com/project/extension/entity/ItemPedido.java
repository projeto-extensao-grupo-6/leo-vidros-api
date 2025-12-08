package com.project.extension.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @NotNull
    @Column(name = "quantidade_solicitada", precision = 18, scale = 5)
    private BigDecimal quantidadeSolicitada;

    @NotNull
    @Column(name = "preco_unitario_negociado", precision = 18, scale = 5)
    private BigDecimal precoUnitarioNegociado;

    @Column(name = "subtotal", precision = 18, scale = 2,
            insertable = false, updatable = false)
    private BigDecimal subtotal;

    private String observacao;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    public ItemPedido(BigDecimal quantidadeSolicitada, BigDecimal precoUnitarioNegociado, BigDecimal subtotal, String observacao) {
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.precoUnitarioNegociado = precoUnitarioNegociado;
        this.subtotal = subtotal;
        this.observacao = observacao;
    }
}