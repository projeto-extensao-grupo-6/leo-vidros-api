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
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_total", precision = 18, scale = 2)
    private BigDecimal quantidadeTotal;

    @Column(name = "quantidade_disponivel", precision = 18, scale = 2)
    private BigDecimal quantidadeDisponivel;

    @Column(name = "reservado", precision = 18, scale = 2)
    private BigDecimal reservado;
    private String localizacao;

    @OneToMany(mappedBy = "estoque")
    private List<ItemPedido> itensPedido = new ArrayList<>();

    public Estoque(String localizacao, BigDecimal quantidadeTotal) {
        this.localizacao = localizacao;
        this.quantidadeTotal = quantidadeTotal;
    }
}
