    package com.project.extension.dto.agendamento;

    import com.project.extension.dto.agendamentoproduto.AgendamentoProdutoMapper;
    import com.project.extension.dto.endereco.EnderecoMapper;
    import com.project.extension.dto.funcionario.FuncionarioMapper;
    import com.project.extension.dto.pedido.PedidoMapper;
    import com.project.extension.dto.status.StatusMapper;
    import com.project.extension.entity.Agendamento;
    import com.project.extension.entity.AgendamentoProduto;
    import com.project.extension.entity.Funcionario;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    import java.util.List;
    import java.util.stream.Collectors;

    @Component
    @RequiredArgsConstructor
    public class AgendamentoMapper {

        private final EnderecoMapper enderecoMapper;
        private final FuncionarioMapper funcionarioMapper;
        private final StatusMapper statusMapper;
        private final PedidoMapper pedidoMapper;
        private final AgendamentoProdutoMapper agendamentoProdutoMapper;

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

            List<AgendamentoProduto> agendamentoProdutos = dto.produtos().stream()
                    .map(agendamentoProdutoMapper::toEntity)
                    .collect(Collectors.toList());

            agendamento.setAgendamentoProdutos(agendamentoProdutos);

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
                    funcionarioMapper.toResponse(agendamento.getFuncionarios()),
                    agendamento.getAgendamentoProdutos().stream()
                            .map(agendamentoProdutoMapper::toResponse)
                            .collect(Collectors.toList())
            );
        }
    }