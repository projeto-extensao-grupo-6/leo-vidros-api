package com.project.extension.strategy.pdf;

import com.project.extension.rabbitmq.queue.PdfResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.environment", havingValue = "production")
public class S3PdfStorageStrategy implements PdfStorageStrategy {

    private static final Pattern NUMERO_VALIDO = Pattern.compile("^[A-Za-z0-9_-]+$");
    private static final String PREFIX = "orcamentos/";
    private static final String SUFFIX = ".pdf";

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public void armazenar(PdfResponse response) {
        String numero = response.getNumeroOrcamento();
        if (numero == null || numero.isBlank()) {
            log.warn("Resposta de PDF sem numeroOrcamento");
            return;
        }
        // O upload já foi feito pelo microserviço; apenas confirmamos o recebimento
        log.info("PDF do orçamento {} disponível no S3 como {}", numero, chaveS3(numero));
    }

    @Override
    public byte[] obterPorNumeroOrcamento(String numero) {
        if (!NUMERO_VALIDO.matcher(numero).matches()) {
            log.warn("Número de orçamento inválido para busca S3: {}", numero);
            return null;
        }

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(chaveS3(numero))
                    .build();

            ResponseBytes<GetObjectResponse> objeto = s3Client.getObjectAsBytes(request);
            return objeto.asByteArray();
        } catch (Exception e) {
            log.error("Falha ao buscar PDF do S3 para orçamento {}", numero, e);
            return null;
        }
    }

    private String chaveS3(String numero) {
        return PREFIX + numero + SUFFIX;
    }
}
