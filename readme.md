# Projeto de Extensão – API Rest

API em Java Spring Boot.

---

## Sumário

* [Getting Started](#getting-started)
* [Suporte ao Docker Compose](#suporte-ao-docker-compose)
* [Arquitetura da Aplicação](#arquitetura-da-aplicação)
* [Flyway Migration](#flyway-migration)
* [Padrões de Desenvolvimento](#padrões-de-desenvolvimento)
* [Lombok](#lombok)
* [Guias e Documentação](#guias-e-documentação)

---

## Getting Started

### Requisitos

* Java 17+
* Maven 3.9+
* Docker / Docker Compose
* Banco de dados MySQL (containerizado)

### Executando localmente

1. Clone o repositório.
2. Configure o arquivo `compose` com as credenciais do banco.
3. Suba os containers com:

```bash
docker-compose up -d
```
4. Rode a aplicação Spring Boot:

5. A API estará disponível em:

```
http://localhost:3000/api
```

---

## Suporte ao Docker Compose

O arquivo `compose.yml` define os serviços auxiliares:

* **MySQL:** `mysql:8.4`
* Porta padrão: `3306` (pode ser alterada no compose)
* Volume persistente configurado para manter os dados entre execuções

Comando para inicialização:

```bash
docker-compose up -d
```

---

## Arquitetura da Aplicação

A aplicação segue uma arquitetura em camadas, organizada da seguinte forma:

* **Controller** – recebe as requisições HTTP e retorna as respostas.
* **Service** – contém as regras de negócio e orquestra o fluxo entre camadas.
* **Repository** – camada de persistência, utilizando Spring Data JPA.
* **DTO (Data Transfer Object)** –

    * `Request`: objetos usados para entrada de dados via API.
    * `Response`: objetos usados para saída de dados da API.
* **Exception** – tratamento centralizado de erros e exceções personalizadas.
* **Config** – classes de configuração do Spring (CORS, beans, segurança, etc.).
* **Util** – funções auxiliares e classes de suporte reutilizáveis.

---

## Flyway Migration

O projeto utiliza **Flyway** para versionamento do banco de dados.
O Flyway garante que todas as mudanças de schema sejam rastreadas, aplicadas e reproduzíveis.

* Scripts SQL ficam em:

```
src/main/resources/db/migration
```

* Nome padrão:

```
V[ANO][MÊS][DIA][HORA][MINUTO]__exemplo_schema.sql

V202508180947__create_tables_schema.sql
```

* Ao iniciar a aplicação, o Flyway executa automaticamente os scripts pendentes, garantindo que o banco esteja sempre sincronizado com o código.

---

## Padrões de Desenvolvimento

Para manter consistência, o código segue os seguintes padrões:

* **Controllers**: anotados com `@RestController`, expõem endpoints em `/api/...`
* **Services**: anotados com `@Service`, contêm lógica de negócio.
* **Repositories**: interfaces que estendem `JpaRepository`.
* **DTOs**: separados em `request` e `response`, evitando expor entidades diretamente.
* **Exceptions**: tratadas via `@ControllerAdvice` para respostas padronizadas.
* **Config**: centraliza configurações do Spring (CORS, segurança, etc.).
* **Utils**: classes estáticas para funções auxiliares.

---

## Lombok

O projeto utiliza **Lombok** para reduzir código boilerplate:

* `@Getter` e `@Setter` – gera automaticamente getters e setters.
* `@Builder` – cria builders para facilitar instanciação de objetos.
* `@NoArgsConstructor` e `@AllArgsConstructor` – gera construtores automaticamente.

> ⚠️ Importante: É necessário instalar o plugin do Lombok na IDE para evitar erros de compilação durante o desenvolvimento.

---

## Guias e Documentação

* [Spring Boot Documentation](https://spring.io/projects/spring-boot)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [Flyway Docs](https://documentation.red-gate.com/fd)
* [Docker Docs](https://docs.docker.com/)
* [Lombok Docs](https://projectlombok.org/)
