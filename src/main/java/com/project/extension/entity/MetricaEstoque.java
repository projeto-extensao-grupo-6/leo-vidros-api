package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metrica_estoque")
@Getter
@Setter
@NoArgsConstructor
public class MetricaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nivel_minimo")
    private Integer nivelMinimo;

    @Column(name = "nivel_maximo")
    private Integer nivelMaximo;

    public MetricaEstoque(Integer nivelMinimo, Integer nivelMaximo) {
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }
}
