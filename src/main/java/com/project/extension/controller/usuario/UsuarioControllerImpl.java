package com.project.extension.controller.usuario;

import com.project.extension.controller.usuario.dto.DefinirSenhaRequestDto;
import com.project.extension.controller.usuario.dto.UsuarioMapper;
import com.project.extension.controller.usuario.dto.UsuarioRequestDto;
import com.project.extension.controller.usuario.dto.UsuarioResponseDto;
import com.project.extension.entity.Usuario;
import com.project.extension.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Override
    public ResponseEntity<Page<UsuarioResponseDto>> buscarTodos(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.buscarTodos(pageable).map(mapper::toResponseDto));
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