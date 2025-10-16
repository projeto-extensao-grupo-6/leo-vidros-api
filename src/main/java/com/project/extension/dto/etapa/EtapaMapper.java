package com.project.extension.dto.etapa;

import com.project.extension.entity.Etapa;
import org.springframework.stereotype.Component;

@Component
public class EtapaMapper {

    public Etapa toEntity(EtapaRequestDto dto) {
        if (dto == null) return null;

        return new Etapa(
                dto.tipo(),
                dto.nome()
        );
    }

    public EtapaResponseDto toResponse(Etapa etapa) {
        if (etapa == null) return null;

        return new EtapaResponseDto(
                etapa.getId(),
                etapa.getTipo(),
                etapa.getNome()
        );
    }
}
