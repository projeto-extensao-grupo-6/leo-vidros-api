package com.project.extension.strategy.agendamento;

import com.project.extension.entity.Agendamento;
import com.project.extension.entity.TipoAgendamento;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgendamentoContext {

    private final AgendamentoOrcamentoStrategy orcamentoStrategy;
    private final AgendamentoServicoStrategy servicoStrategy;

    public Agendamento processarAgendamento(Agendamento agendamento) {
        if (agendamento.getTipoAgendamento() == TipoAgendamento.ORCAMENTO) {
            return orcamentoStrategy.agendar(agendamento);
        } else if (agendamento.getTipoAgendamento() == TipoAgendamento.SERVICO) {
            return servicoStrategy.agendar(agendamento);
        }
        throw new IllegalArgumentException("Tipo de agendamento não suportado");
    }
}
