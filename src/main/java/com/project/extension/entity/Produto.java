package com.project.extension.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String descricao;

    @Column(name = "unidade_medida")
    private String unidademedida;

    private Double preco;
    private Boolean ativo;

    @OneToMany(mappedBy = "produto")
    private List<AtributoProduto> atributos;

    public Produto(String nome, String descricao, String unidademedida, Double preco, Boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.unidademedida = unidademedida;
        this.preco = preco;
        this.ativo = ativo;
    }
}
