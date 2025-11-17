package com.project.extension.dto.solicitacao;

import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Solicitacao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SolicitacaoMapper {

    private final StatusMapper statusMapper;

    public Solicitacao toEntity(SolicitacaoRequestDto dto) {
        if (dto == null) return null;

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setNome(dto.nome());
        solicitacao.setEmail(dto.email());
        solicitacao.setCpf(dto.cpf());
        solicitacao.setTelefone(dto.telefone());
        return solicitacao;
    }

    public SolicitacaoResponseDto toResponse(Solicitacao solicitacao) {
        if (solicitacao == null) return null;

        return new SolicitacaoResponseDto(
                solicitacao.getId(),
                solicitacao.getNome(),
                solicitacao.getCpf(),
                solicitacao.getEmail(),
                solicitacao.getTelefone(),
                statusMapper.toResponse(solicitacao.getStatus())
        );
    }
}
