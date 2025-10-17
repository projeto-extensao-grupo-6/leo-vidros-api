package com.project.extension.service;

import com.project.extension.entity.Status;
import com.project.extension.exception.naoencontrado.StatusNaoEncontradoException;
import com.project.extension.repository.StatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class StatusService {

    private final StatusRepository repository;

    public Status cadastrar(Status status) {
        Status salvo = repository.save(status);
        log.info("Status cadastrado com sucesso: tipo='{}', nome='{}'",
                salvo.getTipo(), salvo.getNome());

        return salvo;
    }

    public Status buscarPorTipoAndStatus(String tipo, String nome) {
        return repository.findByTipoAndNome(tipo, nome).orElseThrow(() -> {
            log.error("Status do tipo:  " + tipo + "e nome: " + nome + "não encontrado");
            return new StatusNaoEncontradoException();
        });
    }

    public Status buscarOuCriarPorTipoENome(String tipo, String nome) {
        return repository.findByTipoAndNome(tipo, nome)
                .orElseGet(() -> {
                    log.warn("Status não encontrado (tipo='{}', nome='{}'). Criando novo...", tipo, nome);
                    Status novo = new Status();
                    novo.setTipo(tipo);
                    novo.setNome(nome);
                    return repository.save(novo);
                });
    }

}

