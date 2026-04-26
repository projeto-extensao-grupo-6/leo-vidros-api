package com.project.extension.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;
import com.project.extension.exception.naopodesernegativo.base.NaoPodeSerNegativoException;
import com.project.extension.service.LogService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message,
                                                      String exception, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        if (exception != null) {
            body.put("exception", exception);
        }
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }

    // ── 400 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (m1, m2) -> m1
                ));
        log.warn("Validação falhou nos campos: {}", campos);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Erro de validação nos campos enviados");
        body.put("campos", campos);
        body.put("path", request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Corpo da requisição inválido ou malformado";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife && ife.getTargetType() != null && ife.getTargetType().isEnum()) {
            String valoresValidos = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            message = String.format("Valor '%s' inválido. Valores aceitos: %s", ife.getValue(), valoresValidos);
        }
        log.warn("Requisição ilegível em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, null, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Parâmetro '%s' com valor '%s' é inválido", ex.getName(), ex.getValue());
        log.warn("Tipo inválido: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, null, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = String.format("Parâmetro obrigatório ausente: '%s'", ex.getParameterName());
        log.warn("Parâmetro ausente: {}", ex.getParameterName());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, null, request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocio(RegraNegocioException ex, HttpServletRequest request) {
        logService.warning(String.format("Regra de Negócio violada (400). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage()));
        log.warn("Regra de negócio: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Argumento inválido em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Estado inválido em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null, request);
    }

    // ── 401 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleAuthentication(Exception ex, HttpServletRequest request) {
        log.warn("Falha de autenticação em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Email ou senha inválidos.", null, request);
    }

    // ── 403 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Acesso negado em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", "Você não tem permissão para acessar este recurso.", null, request);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurity(SecurityException ex, HttpServletRequest request) {
        log.warn("Segurança em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), null, request);
    }

    // ── 404 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Object> handleNaoEncontrado(NaoEncontradoException ex, HttpServletRequest request) {
        logService.error(String.format("Recurso não encontrado (404). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage()));
        log.warn("Não encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null, request);
    }

    // ── 405 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = String.format("Método '%s' não é suportado para este endpoint", ex.getMethod());
        log.warn("Método não suportado em {}: {}", request.getRequestURI(), ex.getMethod());
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", message, null, request);
    }

    // ── 409 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(NaoPodeSerNegativoException.class)
    public ResponseEntity<Object> handleNaoPodeSerNegativo(NaoPodeSerNegativoException ex, HttpServletRequest request) {
        logService.warning(String.format("Conflito de regra de negócio (409). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage()));
        log.warn("Valor negativo: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        logService.error(String.format("Violação de integridade (409). Path: %s. Mensagem: %s",
                request.getRequestURI(), ex.getMessage()));
        log.warn("Violação de integridade em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflict",
                "Conflito de dados: registro já existente ou violação de restrição.", null, request);
    }

    // ── 500 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        logService.error(String.format("Erro não tratado (500). Exceção: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage()));
        log.error("Erro não tratado: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ocorreu um erro interno no servidor.", ex.getClass().getSimpleName(), request);
    }
}
