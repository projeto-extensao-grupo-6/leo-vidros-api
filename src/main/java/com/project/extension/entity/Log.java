package com.project.extension.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora")
    @NotNull
    private LocalDateTime dataHora;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    public Log(LocalDateTime dataHora, String mensagem, Categoria categoria) {
        this.dataHora = dataHora;
        this.mensagem = mensagem;
        this.categoria = categoria;
    }
}
