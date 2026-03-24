package com.project.extension.controller.usuario.dto;

import com.project.extension.controller.valueobject.endereco.EnderecoMapper;
import com.project.extension.entity.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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
                usuario.getSenha(),
                usuario.getTelefone(),
                usuario.getFirstLogin(),
                enderecoMapper.toResponse(usuario.getEndereco())
        );
    }

    public Usuario updateSenha(Usuario usuarioExistente, String novaSenhaCriptografada) {
        if (usuarioExistente == null) return null;

        usuarioExistente.setSenha(novaSenhaCriptografada);
        usuarioExistente.setFirstLogin(false);
        return usuarioExistente;
    }
}