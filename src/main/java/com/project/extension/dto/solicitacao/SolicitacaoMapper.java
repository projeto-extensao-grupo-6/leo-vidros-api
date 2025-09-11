package com.project.extension.dto.solicitacao;

import com.project.extension.entity.Solicitacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitacaoMapper {
    @Mapping(target = "cargoDesejado", ignore = true)
    @Mapping(target = "status", ignore = true)
    Solicitacao toEntity(SolicitacaoRequestDto dto);

    SolicitacaoResponseDto toResponse(Solicitacao solicitacao);
}
