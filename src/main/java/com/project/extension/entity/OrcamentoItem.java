package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orcamento_item")
public class OrcamentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(precision = 18, scale = 5, nullable = false)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", precision = 18, scale = 5, nullable = false)
    private BigDecimal precoUnitario;

    @Column(precision = 18, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column
    private Integer ordem = 0;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}
