package com.project.extension.service;

import com.project.extension.entity.Etapa;
import com.project.extension.exception.naoencontrado.EtapaNaoEncontradoException;
import com.project.extension.repository.EtapaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EtapaService {
    private final EtapaRepository repository;
    private final LogService logService;

    public Etapa cadastrar(Etapa etapa) {
        Etapa salvo = repository.save(etapa);
        String mensagem = String.format("Nova Etapa cadastrada com sucesso. ID: %d, Tipo: '%s', Nome: '%s'.",
                salvo.getId(), salvo.getTipo(), salvo.getNome());
        logService.success(mensagem); // Usando SUCCESS para indicar criação bem-sucedida

        return salvo;
    }

    public Etapa buscarPorTipoAndEtapa(String tipo, String nome) {
        return repository.findByTipoAndNome(tipo, nome).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Etapa do tipo '%s' e nome '%s' não encontrada.", tipo, nome);
            logService.error(mensagem);
            log.warn(mensagem);
            return new EtapaNaoEncontradoException();
        });
    }
}
