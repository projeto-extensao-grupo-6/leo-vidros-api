package com.project.extension.dto.usuario;

import com.project.extension.entity.Role;
import com.project.extension.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "senha", source = "senha")
    @Mapping(target = "role", source = "role")
    Usuario toEntity(UsuarioRequestDto dto);

    UsuarioResponseDto toResponseDto(Usuario usuario);

    default Role map(String roleName) {
        if (roleName == null) return null;

        Role role = new Role();
        if(roleName.equalsIgnoreCase("admin")) {
            role.setId(1);
        } else if(roleName.equalsIgnoreCase("comum")) {
            role.setId(2);
        } else {
            throw new RuntimeException("Role inválida: " + roleName);
        }

        return role;
    }
}