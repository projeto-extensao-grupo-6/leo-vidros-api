package com.project.extension.service.auth;

import com.project.extension.entity.Usuario;
import com.project.extension.exception.naoencontrado.UsuarioNaoEncontradoException;
import com.project.extension.repository.UsuarioRepository;
import com.project.extension.service.AuthService;
import com.project.extension.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private AuthService authService;

    private final String EMAIL_EXISTENTE = "teste@exemplo.com";
    private final String EMAIL_INEXISTENTE = "naoexiste@exemplo.com";
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(EMAIL_EXISTENTE);
        usuario.setSenha("senha_criptografada");
    }

    // Teste 1: Caso de Sucesso - Usuário encontrado e UserDetails retornado
    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(EMAIL_EXISTENTE)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = authService.loadUserByUsername(EMAIL_EXISTENTE);

        // Assert
        assertNotNull(userDetails);
        assertEquals(EMAIL_EXISTENTE, userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        verify(usuarioRepository, times(1)).findByEmail(EMAIL_EXISTENTE);
        verify(logService, times(1)).success(anyString());
    }

    // Teste 2: Caso de Falha - Usuário não encontrado, lança exceção customizada
    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsuarioNaoEncontradoException() {
        // Arrange
        when(usuarioRepository.findByEmail(EMAIL_INEXISTENTE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            authService.loadUserByUsername(EMAIL_INEXISTENTE);
        });

        verify(usuarioRepository, times(1)).findByEmail(EMAIL_INEXISTENTE);
        verify(logService, times(1)).warning(anyString());
    }

    // Teste 3: Verifica o conteúdo exato do UserDetails retornado
    @Test
    void loadUserByUsername_VerifyUserDetailsContent() {
        // Arrange
        when(usuarioRepository.findByEmail(EMAIL_EXISTENTE)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = authService.loadUserByUsername(EMAIL_EXISTENTE);

        // Assert
        assertEquals(usuario.getEmail(), userDetails.getUsername(), "O nome de usuário (email) deve ser o mesmo do usuário encontrado.");
        assertEquals(usuario.getSenha(), userDetails.getPassword(), "A senha deve ser a senha criptografada do usuário.");
        assertTrue(userDetails.getAuthorities().isEmpty(), "A lista de autoridades deve estar vazia.");
    }

    // Teste 4: Verifica a mensagem de log de sucesso
    @Test
    void loadUserByUsername_VerifySuccessLogMessage() {
        // Arrange
        when(usuarioRepository.findByEmail(EMAIL_EXISTENTE)).thenReturn(Optional.of(usuario));
        String expectedMessage = String.format("Login bem-sucedido. Usuário ID %d autenticado com e-mail: %s.",
                usuario.getId(),
                EMAIL_EXISTENTE);

        // Act
        authService.loadUserByUsername(EMAIL_EXISTENTE);

        // Assert
        verify(logService, times(1)).success(expectedMessage);
    }

    // Teste 5: Verifica a mensagem de log de aviso (warning) em caso de falha
    @Test
    void loadUserByUsername_VerifyWarningLogMessage() {
        // Arrange
        when(usuarioRepository.findByEmail(EMAIL_INEXISTENTE)).thenReturn(Optional.empty());
        String expectedMessage = String.format("Tentativa de login falhou. Usuário com e-mail '%s' não encontrado no sistema.", EMAIL_INEXISTENTE);

        // Act & Assert
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            authService.loadUserByUsername(EMAIL_INEXISTENTE);
        });

        verify(logService, times(1)).warning(expectedMessage);
    }
}
