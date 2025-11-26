package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_total")
    private Integer quantidadeTotal;

    @Column(name = "quantidade_disponivel")
    private Integer quantidadeDisponivel;

    private Integer reservado;
    private String localizacao;

    @OneToMany(mappedBy = "estoque")
    private List<ItemPedido> itensPedido = new ArrayList<>();

    public Estoque(String localizacao, Integer quantidadeTotal) {
        this.localizacao = localizacao;
        this.quantidadeTotal = quantidadeTotal;
    }
}
