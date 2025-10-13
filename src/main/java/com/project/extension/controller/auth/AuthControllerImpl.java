package com.project.extension.controller.auth;

import com.project.extension.dto.auth.AuthRequestDto;
import com.project.extension.dto.auth.AuthResponseDto;
import com.project.extension.dto.usuario.UsuarioMapper;
import com.project.extension.entity.Usuario;
import com.project.extension.service.UsuarioService;
import com.project.extension.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthControllerDoc {

    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;
    private final UsuarioMapper mapper;

    @Override
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        autenticar(request);

        Usuario usuario = usuarioService.buscarPorEmail(request.email());
        String token = gerarToken(usuario);

        return ResponseEntity.ok(new AuthResponseDto(token, usuario.getNome(), usuario.getId()));
    }

    private void autenticar(AuthRequestDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.senha()
        ));
    }

    private String gerarToken(Usuario usuario) {
        UserDetails userDetails = new User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of()
        );
        return tokenProvider.gerarToken(userDetails);
    }
}