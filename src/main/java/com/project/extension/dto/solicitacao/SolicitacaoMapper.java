package com.project.extension.dto.solicitacao;

import com.project.extension.entity.Solicitacao;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {

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
                solicitacao.getTelefone()
        );
    }
}
