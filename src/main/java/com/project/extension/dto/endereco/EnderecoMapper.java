package com.project.extension.dto.endereco;

import com.project.extension.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequestDto dto) {
        if (dto == null) return null;

        return new Endereco(
                dto.rua(),
                dto.complemento(),
                dto.cep(),
                dto.cidade(),
                dto.bairro(),
                dto.uf(),
                dto.pais()
        );
    }

    public EnderecoResponseDto toResponse(Endereco endereco) {
        if (endereco == null) return null;

        return new EnderecoResponseDto(
                endereco.getId(),
                endereco.getRua(),
                endereco.getComplemento(),
                endereco.getCep(),
                endereco.getCidade(),
                endereco.getBairro(),
                endereco.getUf(),
                endereco.getPais()
        );
    }
}
