package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private TipoAgendamento tipoAgendamento;
    private LocalDateTime dataAgendamento;
    private StatusAgendamento statusAgendamento;
    private String observacao;

    @ManyToOne
    private Endereco endereco;

    public Agendamento(TipoAgendamento tipoAgendamento, LocalDateTime dataAgendamento, StatusAgendamento statusAgendamento, String observacao) {
        this.tipoAgendamento = tipoAgendamento;
        this.dataAgendamento = dataAgendamento;
        this.statusAgendamento = statusAgendamento;
        this.observacao = observacao;
    }
}
