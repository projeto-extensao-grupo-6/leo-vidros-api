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
public class HistoricoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "quantidade", precision = 18, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "quantidade_atual", precision = 18, scale = 2)
    private BigDecimal quantidadeAtual;

    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    private OrigemMovimentacao origem;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_perda")
    private MotivoPerda motivoPerda;

    @Column(name = "data_movimentacao", insertable = false, updatable = false)
    private LocalDateTime dataMovimentacao;
}
