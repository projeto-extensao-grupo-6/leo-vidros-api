package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String email;
    private String cpf;
    private String senha;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
