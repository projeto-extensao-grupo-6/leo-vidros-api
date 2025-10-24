package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer quantidade;
    private Integer reservado;
    private String localizacao;

    public Estoque( Integer quantidade, Integer reservado, String localizacao) {
        this.quantidade = quantidade;
        this.reservado = reservado;
        this.localizacao = localizacao;
    }
}
