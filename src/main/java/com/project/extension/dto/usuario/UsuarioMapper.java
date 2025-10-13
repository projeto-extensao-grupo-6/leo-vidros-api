package com.project.extension.dto.usuario;

import com.project.extension.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) return null;

        return new Usuario(
                dto.nome(),
                dto.email(),
                dto.cpf(),
                dto.senha(),
                dto.telefone(),
                true
        );
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    public List<UsuarioResponseDto> toResponseList(List<Usuario> usuarios) {
        if (usuarios == null) return List.of();
        return usuarios.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}