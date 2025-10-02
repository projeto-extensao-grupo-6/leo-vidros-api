package com.project.extension.dto.servico;

import com.project.extension.entity.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServicoMapper {

    Servico toEntity(ServicoRequestDto dto);

    ServicoResponseDto toResponse(Servico servico);
}
