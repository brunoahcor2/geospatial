# Geospatial API

Aplicação **Geospatial API** desenvolvida em **Java 21** com **Spring Boot 4**.  
Fornece endpoints REST para gerenciamento de pessoas (`Person`) com regras de negócio, integração via **OpenAPI** e testes unitários.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 4.x
- Spring Web MVC
- Spring Test (MockMvc)
- Lombok
- Springdoc OpenAPI
- Docker
- Maven (gerenciamento de dependências)

---

## Funcionalidades

- CRUD de `Person`
- Consulta de idade em **dias, meses ou anos**
- Consulta de salário em **valor total ou salários mínimos**
- Tratamento global de exceções via `GlobalExceptionHandler`
- Documentação automática via OpenAPI/Swagger (`/v3/api-docs` e `/swagger-ui.html`)
- Testes unitários utilizando **MockMvc** e **Mockito**
- Configuração Docker para build e execução em container

---

## Endpoints Principais

- `GET /v1/persons` – Lista todas as pessoas
- `GET /v1/persons/{id}` – Busca pessoa por ID
- `POST /v1/persons` – Cria uma nova pessoa
- `PUT /v1/persons/{id}` – Atualiza uma pessoa
- `PATCH /v1/persons/{id}` – Atualiza parcialmente
- `DELETE /v1/persons/{id}` – Remove uma pessoa
- `GET /v1/persons/{id}/age?output=YEARS` – Calcula idade
- `GET /v1/persons/{id}/salary?output=FULL` – Calcula salário

---

## Como rodar a aplicação localmente

### Pré-requisitos

- Java 21
- Maven 3.x
- Docker Desktop (opcional para container)

---

### 1. Rodando com Maven

```bash
mvn clean install
mvn spring-boot:run
```

### 2. Rodando com Script SH em ambiente Linux

```bash
chmod +x run-docker.sh
./run-docker.sh
```

### 3. Rodando com Script BAT em ambiente Windows

```bash
run-docker.bat
```