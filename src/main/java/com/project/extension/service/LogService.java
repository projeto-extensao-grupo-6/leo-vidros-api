package com.project.extension.service;

import com.project.extension.entity.Categoria;
import com.project.extension.entity.Log;
import com.project.extension.repository.CategoriaRepository;
import com.project.extension.repository.LogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final CategoriaRepository categoriaRepository;

    // Mapa para armazena em cache as categorias, evitando buscas repetidas no banco de dados
    private final Map<String, Categoria> categoriaCache = new HashMap<>();

    public LogService(LogRepository logRepository, CategoriaRepository categoriaRepository) {
        this.logRepository = logRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Carrega as categorias existentes no banco de dados para o cache ao iniciar o serviço
    @PostConstruct
    public void inicializar()
    {
        categoriaRepository.findAll().forEach(categoria -> {
            categoriaCache.put(categoria.getNome().toUpperCase(), categoria);
        });
    }

    /*
     * Metodo genérico para persistir o log.
     * @param nivel O nome da categoria (ex: "INFO", "ERROR").
     * @param mensagem A mensagem de log.
     * @param throwable (Opcional) A exceção associada, se houver.
     */
    private void salvarLog(String nivel, String mensagem, Throwable throwable)
    {
        Categoria categoria = categoriaCache.get(nivel.toUpperCase());

        if (categoria == null) {
            System.err.println("Categoria de log " + nivel + " não encontrada. Usando INFO como fallback.");
            categoriaCache.get("INFO");
        }

        String mensagemCompleta = mensagem;
        if (throwable != null) {
            mensagemCompleta += "\nStack Trace: " + formatarStackTrace(throwable);
        }

        Log novoLog = new Log(LocalDateTime.now(), mensagemCompleta, categoria);
        logRepository.save(novoLog);
    }

    private String formatarStackTrace(Throwable t)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString()).append("\n");
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("\t").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    public void info(String mensagem)
    {
        salvarLog("INFO", mensagem, null);
    }

    public void success(String mensagem)
    {
        salvarLog("SUCCESS", mensagem, null);
    }

    public void error(String mensagem, Throwable e)
    {
        salvarLog("ERROR", mensagem, e);
    }

    public void error(String mensagem)
    {
        salvarLog("ERROR", mensagem, null);
    }

    public void debug(String mensagem)
    {
        salvarLog("DEBUG", mensagem, null);
    }

    public void warning(String mensagem)
    {
        salvarLog("WARNING", mensagem, null);
    }

    public void fatal(String mensagem, Throwable e)
    {
        salvarLog("FATAL", mensagem, e);
    }

}
