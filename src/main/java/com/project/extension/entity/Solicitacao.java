package com.project.extension.entity;

import com.project.extension.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    @Column(name = "cargo_desejado")
    private String cargoDesejado;

    private Status status;
}
