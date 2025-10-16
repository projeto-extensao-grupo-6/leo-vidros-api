package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String telefone;
    private String funcao;
    private String contrato;
    private Boolean ativo;

    @ManyToMany(mappedBy = "funcionarios", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;

    public Funcionario(String nome, String telefone, String funcao, String contrato, Boolean ativo) {
        this.nome = nome;
        this.telefone = telefone;
        this.funcao = funcao;
        this.contrato = contrato;
        this.ativo = ativo;
    }
}
