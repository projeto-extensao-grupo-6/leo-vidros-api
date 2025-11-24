package com.project.extension.controller.usuario;

import com.project.extension.dto.usuario.DefinirSenhaRequestDto;
import com.project.extension.dto.usuario.UsuarioMapper;
import com.project.extension.dto.usuario.UsuarioRequestDto;
import com.project.extension.dto.usuario.UsuarioResponseDto;
import com.project.extension.entity.Usuario;
import com.project.extension.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioControllerImpl implements UsuarioControllerDoc {

    private final UsuarioMapper mapper;
    private final UsuarioService service;

    @Override
    public ResponseEntity<UsuarioResponseDto> salvar(UsuarioRequestDto request) {
        Usuario usuarioSalvar = mapper.toEntity(request);
        Usuario usuarioSalvo = service.salvar(usuarioSalvar);

        return ResponseEntity.status(201).body(mapper.toResponseDto(usuarioSalvo));
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> buscarPorId(Integer id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponseDto(usuario));
    }

    public ResponseEntity<List<UsuarioResponseDto>> buscarTodos() {
        List<Usuario> usuarios = service.buscarTodos();

        return usuarios.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(usuarios.stream()
                .map(mapper::toResponseDto)
                .toList());
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> atualizar(UsuarioRequestDto request, Integer id) {
        Usuario usuarioAtualizar = mapper.toEntity(request);
        Usuario usuarioAtualizado = service.editar(usuarioAtualizar, id);
        return ResponseEntity.status(200).body(mapper.toResponseDto(usuarioAtualizado));
    }

    @Override
    public ResponseEntity<Void> definirSenhaInicial(DefinirSenhaRequestDto request) {
        service.definirSenhaInicial(request.idUsuario(), request.novaSenha());
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Usuário deletado com sucesso.");
    }
}