package com.project.extension.dto.endereco;

import com.project.extension.entity.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnderecoMapper {

    Endereco toEntity(EnderecoRequestDto dto);

    EnderecoResponseDto toResponse(Endereco endereco);
}
