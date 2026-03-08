package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "numero_orcamento", length = 50)
    private String numeroOrcamento;

    @Column(name = "data_orcamento", nullable = false)
    private LocalDate dataOrcamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "prazo_instalacao", length = 100)
    private String prazoInstalacao;

    @Column(length = 100)
    private String garantia;

    @Column(name = "forma_pagamento", length = 255)
    private String formaPagamento;

    @Column(name = "valor_subtotal", precision = 18, scale = 2)
    private BigDecimal valorSubtotal = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 18, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_total", precision = 18, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "pdf_path", length = 500)
    private String pdfPath;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoItem> itens = new ArrayList<>();

    public void adicionarItem(OrcamentoItem item) {
        item.setOrcamento(this);
        this.itens.add(item);
    }
}
