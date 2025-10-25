package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "quantidade_utilizada")
    private Integer quantidadeUtilizada;

    @Column(name = "quantidade_reservada")
    private Integer quantidadeReservada;

    public AgendamentoProduto(Integer quantidadeUtilizada, Integer quantidadeReservada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.quantidadeReservada = quantidadeReservada;
    }
}