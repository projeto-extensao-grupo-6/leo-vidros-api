package com.project.extension.dto.usuario;

import com.project.extension.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "role", ignore = true)
    Usuario toEntity(UsuarioRequestDto dto);

    UsuarioResponseDto toResponseDto(Usuario usuario);
}