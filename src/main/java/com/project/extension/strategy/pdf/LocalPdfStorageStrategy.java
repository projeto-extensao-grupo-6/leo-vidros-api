package com.project.extension.strategy.pdf;

import com.project.extension.rabbitmq.queue.PdfResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.environment", havingValue = "development", matchIfMissing = true)
public class LocalPdfStorageStrategy implements PdfStorageStrategy {

    private final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    @Value("${app.pdf.storage-path:resources/pdfs}")
    private String storagePath;

    @Override
    public void armazenar(PdfResponse response) {
        String numero = response.getNumeroOrcamento();
        byte[] bytes = response.getPdfBytes();

        if (bytes == null) {
            log.warn("PDF recebido sem bytes para orçamento {}", numero);
            return;
        }

        cache.put(chave(numero), bytes);

        try {
            salvarEmDisco(numero, bytes);
        } catch (IOException e) {
            log.error("Falha ao salvar PDF em disco para orçamento {}", numero, e);
        }
    }

    @Override
    public byte[] obterPorNumeroOrcamento(String numero) {
        byte[] bytes = cache.get(chave(numero));
        if (bytes != null) return bytes;

        try {
            bytes = lerDoDisco(numero);
            if (bytes != null) cache.put(chave(numero), bytes);
        } catch (IOException e) {
            log.error("Falha ao ler PDF do disco para orçamento {}", numero, e);
        }

        return bytes;
    }

    private String chave(String numero) {
        return "orcamento_" + numero;
    }

    private void salvarEmDisco(String numero, byte[] bytes) throws IOException {
        Path dir = Paths.get(storagePath);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path arquivo = dir.resolve("orcamento_" + numero + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(arquivo.toFile())) {
            fos.write(bytes);
        }
    }

    private byte[] lerDoDisco(String numero) throws IOException {
        Path arquivo = Paths.get(storagePath).resolve("orcamento_" + numero + ".pdf");
        if (!Files.exists(arquivo)) return null;
        return Files.readAllBytes(arquivo);
    }
}
