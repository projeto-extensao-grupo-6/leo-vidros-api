package com.project.extension.controller.valueobject.endereco;

import com.project.extension.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequestDto dto) {
        if (dto == null) return null;

        Integer numeroInt = null;
        if (dto.numero() != null && !dto.numero().isBlank()) {
            try {
                numeroInt = Integer.parseInt(dto.numero().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("O campo 'numero' deve ser numérico quando preenchido.", e);
            }
        }

        return new Endereco(
                dto.rua(),
                dto.complemento(),
                dto.cep(),
                dto.cidade(),
                dto.bairro(),
                dto.uf(),
                dto.pais(),
                numeroInt
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
                endereco.getPais(),
                endereco.getNumero()
        );
    }

    public Endereco toEntity(EnderecoResponseDto dto) {
        if (dto == null) return null;

        return new Endereco(
                dto.rua(),
                dto.complemento(),
                dto.cep(),
                dto.cidade(),
                dto.bairro(),
                dto.uf(),
                dto.pais(),
                dto.numero()
        );
    }
}
