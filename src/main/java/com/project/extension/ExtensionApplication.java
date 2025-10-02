package com.project.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.Subject;

@SpringBootApplication
public class ExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtensionApplication.class, args);
    }
    // TODO:
    // criar dto para endereço e serviço: x
    // criar alocação de funcionario e fazer interagir com serviço
    // criar mais metodos para serviço como: buscar agendamento por dia, por horario, por mes
    // criar tabelas
    // padrao de projeto observer:
    //Sujeito (Subject): o Agendamento.
    //Observadores (Observers): os Clientes e os Funcionários alocados.
    //Eventos:
    //Quando o agendamento é criado → notificar cliente e funcionário.
    //Quando faltar 1 semana → notificar cliente e funcionário.
    //No mesmo dia → notificar cliente e funcionário.
}
