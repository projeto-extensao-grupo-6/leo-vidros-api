package com.project.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtensionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtensionApplication.class, args);
	}
    // TODO:
    // criar dto para endereço e serviço
    // criar controller para endereço
    // criar alocação de funcionario e fazer interagir com serviço
    // criar mais metodos para serviço como: buscar agendamento por dia, por horario, por mes
    // criar tabelas
}
