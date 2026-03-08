package com.project.extension.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de Server-Sent Events (SSE) para enviar atualizações de progresso
 * da geração de orçamento/PDF em tempo real para o frontend.
 *
 * Eventos possíveis:
 * - GERANDO_ORCAMENTO: Orçamento está sendo salvo no banco
 * - GERANDO_PDF: Mensagem enviada ao RabbitMQ, PDF em processamento
 * - FINALIZADO: PDF gerado com sucesso
 * - ERRO: Ocorreu um erro em alguma etapa
 */
@Service
@Slf4j
public class OrcamentoSseService {

    // Chave: orcamentoId (ou pedidoId antes do orçamento existir), Valor: SseEmitter
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * Cria e registra um novo emitter SSE para acompanhar o progresso.
     * Timeout de 5 minutos.
     */
    public SseEmitter criarEmitter(String chave) {
        SseEmitter emitter = new SseEmitter(300_000L); // 5 minutos

        emitter.onCompletion(() -> {
            emitters.remove(chave);
            log.debug("SSE emitter completado para chave: {}", chave);
        });

        emitter.onTimeout(() -> {
            emitters.remove(chave);
            log.debug("SSE emitter timeout para chave: {}", chave);
        });

        emitter.onError(e -> {
            emitters.remove(chave);
            log.debug("SSE emitter erro para chave: {}", chave);
        });

        emitters.put(chave, emitter);
        return emitter;
    }

    /**
     * Envia um evento SSE para o cliente conectado.
     */
    public void enviarEvento(Integer id, String status) {
        enviarEventoPorChave(String.valueOf(id), status);
    }

    public void enviarEventoPorChave(String chave, String status) {
        SseEmitter emitter = emitters.get(chave);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("orcamento-progress")
                        .data(Map.of(
                                "id", chave,
                                "status", status
                        ))
                );
                log.debug("Evento SSE enviado - chave: {}, status: {}", chave, status);

                if ("FINALIZADO".equals(status) || "ERRO".equals(status)) {
                    emitter.complete();
                    emitters.remove(chave);
                }
            } catch (IOException e) {
                log.warn("Falha ao enviar evento SSE para chave {}: {}", chave, e.getMessage());
                emitters.remove(chave);
            }
        } else {
            log.debug("Nenhum emitter SSE encontrado para chave: {}. Evento '{}' não enviado.", chave, status);
        }
    }
}
