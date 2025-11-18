package com.project.extension.dto.usuario;

import com.project.extension.dto.endereco.EnderecoMapper;
import com.project.extension.entity.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsuarioMapper {

    private EnderecoMapper enderecoMapper;

    public Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario(
                dto.nome(),
                dto.email(),
                dto.cpf(),
                dto.senha(),
                dto.telefone(),
                true
        );

        usuario.setEndereco(enderecoMapper.toEntity(dto.endereco()));

        return usuario;
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone(),
                enderecoMapper.toResponse(usuario.getEndereco())
        );
    }
}