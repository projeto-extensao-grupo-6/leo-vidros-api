package com.project.extension.exception;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;
import com.project.extension.exception.naopodesernegativo.base.NaoPodeSerNegativoException;
import com.project.extension.service.LogService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Hidden
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final LogService logService;

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Object> handleNaoEncontrado(NaoEncontradoException ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format("Acesso falhou (404 NOT FOUND). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage());
        logService.error(mensagemAuditoria);
        log.warn("Exceção de Recurso Não Encontrado: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NaoPodeSerNegativoException.class)
    public ResponseEntity<Object> handleNaoPodeSerNegativo(NaoPodeSerNegativoException ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format("Conflito de Regra de Negócio (409 CONFLICT). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage());
        logService.warning(mensagemAuditoria);
        log.warn("Exceção de Regra de Negócio/Conflito: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format(
                "Erro não tratado (500 INTERNAL SERVER ERROR). Exceção: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage()
        );

        logService.error(mensagemAuditoria);
        log.error("Erro não tratado: ", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocorreu um erro interno no servidor.");
        body.put("exception", ex.getClass().getSimpleName());
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
