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

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Object> handleNaoEncontrado(NaoEncontradoException ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format("Acesso falhou (404 NOT FOUND). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage());
        logService.error(mensagemAuditoria);
        log.warn("Exceção de Recurso Não Encontrado: {}", ex.getMessage());
        
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null, request);
    }

    @ExceptionHandler(NaoPodeSerNegativoException.class)
    public ResponseEntity<Object> handleNaoPodeSerNegativo(NaoPodeSerNegativoException ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format("Conflito de Regra de Negócio (409 CONFLICT). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage());
        logService.warning(mensagemAuditoria);
        log.warn("Exceção de Regra de Negócio/Conflito: {}", ex.getMessage());
        
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null, request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocio(RegraNegocioException ex, HttpServletRequest request) {
        String mensagemAuditoria = String.format("Regra de Negócio violada (400 BAD REQUEST). Erro: %s. Path: %s. Mensagem: %s",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                ex.getMessage());
        logService.warning(mensagemAuditoria);
        log.warn("Exceção de Regra de Negócio: {}", ex.getMessage());
        
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null, request);
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

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 
                "Ocorreu um erro interno no servidor.", ex.getClass().getSimpleName(), request);
    }
}
