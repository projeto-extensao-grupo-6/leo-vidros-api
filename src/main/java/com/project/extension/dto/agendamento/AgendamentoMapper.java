    package com.project.extension.dto.agendamento;

    import com.project.extension.dto.endereco.EnderecoMapper;
    import com.project.extension.dto.funcionario.FuncionarioMapper;
    import com.project.extension.dto.pedido.PedidoMapper;
    import com.project.extension.dto.status.StatusMapper;
    import com.project.extension.entity.Agendamento;
    import com.project.extension.entity.Funcionario;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class AgendamentoMapper {

        private final EnderecoMapper enderecoMapper;
        private final FuncionarioMapper funcionarioMapper;
        private final StatusMapper statusMapper;
        private final PedidoMapper pedidoMapper;

        public Agendamento toEntity(AgendamentoRequestDto dto) {
            if (dto == null) return null;

            Agendamento agendamento = new Agendamento(
                    dto.tipoAgendamento(),
                    dto.dataAgendamento(),
                    dto.observacao()
            );

            agendamento.setEndereco(enderecoMapper.toEntity(dto.endereco()));

            agendamento.setStatusAgendamento(statusMapper.toEntity(dto.statusAgendamento()));

            agendamento.setPedido(pedidoMapper.toEntity(dto.pedido()));

            List<Funcionario> funcionarios = funcionarioMapper.toEntity(dto.funcionarios());
            agendamento.setFuncionarios(funcionarios);

            return agendamento;
        }

        public AgendamentoResponseDto toResponse(Agendamento agendamento) {
            if (agendamento == null) return null;

            return new AgendamentoResponseDto(
                    agendamento.getId(),
                    agendamento.getTipoAgendamento(),
                    agendamento.getDataAgendamento(),
                    statusMapper.toResponse(agendamento.getStatusAgendamento()),
                    agendamento.getObservacao(),
                    pedidoMapper.toResponse(agendamento.getPedido()),
                    enderecoMapper.toResponse(agendamento.getEndereco()),
                    funcionarioMapper.toResponse(agendamento.getFuncionarios())
            );
        }
    }