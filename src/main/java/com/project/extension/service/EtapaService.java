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

    public Etapa cadastrar(Etapa etapa) {
        Etapa salvo = repository.save(etapa);
        log.info("Etapa cadastrada com sucesso: tipo='{}', nome='{}'",
                salvo.getTipo(), salvo.getNome());

        return salvo;
    }

    public Etapa buscarPorTipoAndEtapa(String tipo, String nome) {
        return repository.findByTipoAndNome(tipo, nome).orElseThrow(() -> {
            log.error("Etapa do tipo:  " + tipo + "e nome: " + nome + "não encontrado");
            return new EtapaNaoEncontradoException();
        });
    }
}
