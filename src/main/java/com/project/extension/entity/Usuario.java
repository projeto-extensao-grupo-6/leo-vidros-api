package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Boolean firstLogin;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
