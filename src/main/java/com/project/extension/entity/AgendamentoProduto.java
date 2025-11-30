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
@Table(name = "agendamento_produto")
public class AgendamentoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_utilizada", precision = 18, scale = 2)
    private BigDecimal quantidadeUtilizada;

    @Column(name = "quantidade_reservada", precision = 18, scale = 2)
    private BigDecimal quantidadeReservada;

    public AgendamentoProduto(BigDecimal quantidadeUtilizada, BigDecimal quantidadeReservada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.quantidadeReservada = quantidadeReservada;
    }
}