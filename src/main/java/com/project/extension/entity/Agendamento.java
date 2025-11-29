package com.project.extension.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_agendamento")
    private TipoAgendamento tipoAgendamento;

    private LocalDate dataAgendamento;

    @Column(name = "inicio_agendamento")
    private LocalTime inicioAgendamento;

    @Column(name = "fim_agendamento")
    private LocalTime fimAgendamento;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statusAgendamento;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "agendamento_funcionario",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private List<Funcionario> funcionarios = new ArrayList<>();

    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgendamentoProduto> agendamentoProdutos = new ArrayList<>();

    public Agendamento(TipoAgendamento tipoAgendamento, LocalDate dataAgendamento,
                        LocalTime inicioAgendamento,  LocalTime fimAgendamento,
                        String observacao) {
        this.tipoAgendamento = tipoAgendamento;
        this.dataAgendamento = dataAgendamento;
        this.inicioAgendamento = inicioAgendamento;
        this.fimAgendamento = fimAgendamento;
        this.observacao = observacao;
    }
}
