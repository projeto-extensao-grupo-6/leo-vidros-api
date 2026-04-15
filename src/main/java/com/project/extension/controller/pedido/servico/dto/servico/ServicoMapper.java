package com.project.extension.controller.pedido.servico.dto.servico;

import com.project.extension.controller.valueobject.etapa.EtapaMapper;
import com.project.extension.controller.pedido.servico.dto.servico.agendamento.AgendamentoServicoMapper;
import com.project.extension.entity.Etapa;
import com.project.extension.entity.Servico;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServicoMapper {

    private final EtapaMapper etapaMapper;
    private final AgendamentoServicoMapper agendamentoServicoMapper;

    public Servico toEntity(ServicoRequestDto dto) {
        if (dto == null) return null;

        Servico servico = new Servico(
                dto.nome(),
                dto.descricao(),
                dto.precoBase(),
                dto.ativo()
        );

        if (dto.etapaNome() != null && !dto.etapaNome().isBlank()) {
            Etapa etapaStub = new Etapa();
            etapaStub.setNome(dto.etapaNome());
            servico.setEtapa(etapaStub);
        }

        return servico;
    }

    public ServicoResponseDto toResponse(Servico servico) {
        if (servico == null) return null;

        return new ServicoResponseDto(
                servico.getId(),
                servico.getCodigo(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPrecoBase(),
                servico.getAtivo(),
                servico.getCreatedAt(),
                etapaMapper.toResponse(servico.getEtapa()),
                servico.getAgendamentos().stream().map(agendamentoServicoMapper::toResponse).toList()
        );
    }

    public Servico toEntity(ServicoResponseDto dto) {
        if (dto == null) return null;

        Servico servico = new Servico(
                dto.nome(),
                dto.descricao(),
                dto.precoBase(),
                dto.ativo()
        );

        servico.setId(dto.id());
        servico.setCodigo(dto.codigo());
        servico.setEtapa(etapaMapper.toEntity(dto.etapa()));

        return servico;
    }
}
