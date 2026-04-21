package com.project.extension.config;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Status;
import com.project.extension.repository.EtapaRepository;
import com.project.extension.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final EtapaRepository etapaRepository;
    private final StatusRepository statusRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedEtapas();
        seedStatus();
    }

    private void seedEtapas() {
        List<String[]> etapas = List.of(
                new String[]{"PEDIDO", "PENDENTE"},
                new String[]{"PEDIDO", "AGUARDANDO ORÇAMENTO"},
                new String[]{"PEDIDO", "ANÁLISE DO ORÇAMENTO"},
                new String[]{"PEDIDO", "ORÇAMENTO APROVADO"},
                new String[]{"PEDIDO", "SERVIÇO AGENDADO"},
                new String[]{"PEDIDO", "SERVIÇO EM EXECUÇÃO"},
                new String[]{"PEDIDO", "CONCLUÍDO"}
        );

        for (String[] e : etapas) {
            if (etapaRepository.findByTipoAndNome(e[0], e[1]).isEmpty()) {
                etapaRepository.save(new Etapa(e[0], e[1]));
                log.info("Etapa inserida: {} - {}", e[0], e[1]);
            }
        }
    }

    private void seedStatus() {
        List<String[]> statuses = List.of(
                new String[]{"PEDIDO", "ATIVO"},
                new String[]{"PEDIDO", "EM ANDAMENTO"},
                new String[]{"PEDIDO", "FINALIZADO"},
                new String[]{"PEDIDO", "PENDENTE"},
                new String[]{"PEDIDO", "CANCELADO"},
                new String[]{"AGENDAMENTO", "PENDENTE"},
                new String[]{"AGENDAMENTO", "EM ANDAMENTO"},
                new String[]{"AGENDAMENTO", "CONCLUÍDO"},
                new String[]{"AGENDAMENTO", "CANCELADO"}
        );

        for (String[] s : statuses) {
            if (statusRepository.findByTipoAndNome(s[0], s[1]).isEmpty()) {
                statusRepository.save(new Status(s[0], s[1]));
                log.info("Status inserido: {} - {}", s[0], s[1]);
            }
        }
    }
}
