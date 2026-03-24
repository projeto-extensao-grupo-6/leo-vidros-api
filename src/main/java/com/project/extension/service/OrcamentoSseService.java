package com.project.extension.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrcamentoSseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, String> pendentes = new ConcurrentHashMap<>();

    public SseEmitter criarEmitter(String chave) {
        SseEmitter emitter = new SseEmitter(300_000L); // 5 minutos

        emitter.onCompletion(() -> {
            emitters.remove(chave);
        });

        emitter.onTimeout(() -> {
            emitters.remove(chave);
        });

        emitter.onError(e -> {
            emitters.remove(chave);
        });

        emitters.put(chave, emitter);

        String pendente = pendentes.remove(chave);
        if (pendente != null) {
            enviarEventoPorChave(chave, pendente);
        }
        return emitter;
    }

    public void enviarEvento(Integer id, String status) {
        enviarEventoPorChave(String.valueOf(id), status);
    }

    public void enviarEventoPorChave(String chave, String status) {
        SseEmitter emitter = emitters.get(chave);
        if (emitter != null) {
            try {
                
                Map<String, Object> eventData = Map.of(
                        "id", chave,
                        "status", status
                );
                
                
                emitter.send(SseEmitter.event()
                        .name("orcamento-progress")
                        .data(eventData)
                );

                if ("FINALIZADO".equals(status) || "ERRO".equals(status)) {
                    emitter.complete();
                    emitters.remove(chave);
                }
            } catch (IOException e) {
                emitters.remove(chave);
            }
        } else {
            pendentes.put(chave, status);
        }
    }
}
