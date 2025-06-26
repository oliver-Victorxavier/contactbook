<div align="center">

# 📇 API de Agenda de Contatos (ContactBook)

<p>
  <a href="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/prod.yml">
    <img src="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/prod.yml/badge.svg" alt="Build Status">
  </a>
  <a href="https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  </a>
</p>

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo"  />
  <img width="12" />
  <img src="https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=black&style=for-the-badge" height="40" alt="spring logo"  />
  <img width="12" />
  <img src="https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white&style=for-the-badge" height="40" alt="docker logo"  />
  <img width="12" />
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white&style=for-the-badge" height="40" alt="postgresql logo"  />
</div>

</div>

API RESTful para gerenciamento de contatos, construída com Java e Spring Boot. O projeto é totalmente containerizado com Docker e possui um pipeline de CI/CD configurado com GitHub Actions para automação de build e deploy.

---

## 📋 Tabela de Conteúdos

- [Visão Geral](#-visão-geral)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Começando](#-começando)
- [Documentação da API](#-documentação-da-api)
- [Autor e Contato](#-autor-e-contato)
- [Licença](#-licença)

---

## 🖼️ Visão Geral

Abaixo, uma prévia da documentação interativa da API gerada com o Swagger.

<div align="center">
  <img src="URL_DA_SUA_IMAGEM_AQUI" alt="Swagger UI Preview" width="80%">
</div>

---

## ✨ Funcionalidades

-   **✅ CRUD Completo:** Operações para Criar, Ler, Atualizar e Deletar contatos.
-   **📍 Endereçamento Automático:** Integração com a API [ViaCEP](https://viacep.com.br/) para preencher o endereço completo.
-   **🔍 Busca e Paginação:** Listagem de contatos com suporte a paginação, ordenação e busca.
-   **⬆️ Importação / ⬇️ Exportação:** Importe contatos via `.csv` e exporte para `.pdf` e `.xlsx`.
-   **🐳 Ambiente Containerizado:** Todo o ambiente (API, Banco de Dados, Admin) orquestrado com Docker Compose.
-   **🚀 CI/CD Automatizado:** Pipeline com GitHub Actions que testa, constrói e publica a imagem no Docker Hub.
-   **📚 Documentação Interativa:** API documentada com Swagger (OpenAPI 3.0).

---

## 📂 Arquitetura do Projeto

O projeto foi estruturado utilizando princípios de **Arquitetura Limpa (Clean Architecture)** e **Design Orientado a Domínio (DDD)**, separando as responsabilidades em quatro camadas principais: `presentation`, `application`, `domain` e `infrastructure`.

<details>
  <summary>Clique para expandir e ver a estrutura do código</summary>

```bash
└── src/main/java/com/victorxavier/contactbook
    ├── presentation
    │   └── controller      # Camada de Apresentação (Controllers REST)
    ├── application
    │   ├── dto             # Data Transfer Objects (Requests/Responses)
    │   ├── mapper          # Mapeamento entre DTOs e Entidades
    │   └── service         # Orquestração e casos de uso da aplicação
    ├── domain
    │   ├── entity          # Entidades e regras de negócio do domínio
    │   ├── repository      # Interfaces de repositório (contratos de persistência)
    │   └── service         # Serviços de domínio (lógica de negócio core)
    ├── infrastructure
    │   ├── client          # Clientes para serviços externos (ex: ViaCEP)
    │   ├── exception       # Tratamento global de exceções
    │   └── service         # Implementações de serviços de infra (exportação, etc.)
    └── ContactbookApplication.java
```

</details>

---

## 💻 Tecnologias Utilizadas

| Categoria      | Tecnologia                                                              |
| -------------- | ----------------------------------------------------------------------- |
| **Back-end**   | Java 21, Spring Boot 3, Spring Data JPA, Maven                          |
| **Banco de Dados** | PostgreSQL                                                              |
| **DevOps**     | Docker, Docker Compose, GitHub Actions                                  |
| **Documentação** | Swagger (OpenAPI 3.0)                                                   |

---

## 🚀 Começando

Siga os passos abaixo para executar o projeto em seu ambiente local.

### Pré-requisitos
-   [Git](https://git-scm.com/)
-   [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)

### Executando com Docker

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/oliver-Victorxavier/contactbook.git
    cd contactbook
    ```

2.  **Inicie os contêineres:**
    Este comando irá construir a imagem (se necessário) e iniciar os serviços da API, do banco de dados e do PgAdmin.
    ```bash
    docker-compose up -d
    ```

3.  **Acesse os serviços:**
    -   **API:** `http://localhost:8080`
    -   **Documentação (Swagger):** `http://localhost:8080/swagger-ui/index.html`
    -   **PgAdmin (Admin do Banco):** `http://localhost:16543` (login: `admin@gmail.com`, senha: `admin`)

---

## 📖 Documentação da API

A documentação completa e interativa dos endpoints está disponível via Swagger UI após a inicialização do projeto.

<details>
  <summary>Clique para expandir e ver os principais endpoints</summary>

| Método | Endpoint                    | Descrição                                 |
| :----- | :-------------------------- | :---------------------------------------- |
| `GET`  | `/api/contacts`             | Lista contatos com paginação e busca.     |
| `POST` | `/api/contacts`             | Cria um novo contato.                     |
| `GET`  | `/api/contacts/{id}`        | Busca um contato por ID.                  |
| `PUT`  | `/api/contacts/{id}`        | Atualiza um contato por ID.               |
| `DELETE`| `/api/contacts/{id}`        | Deleta um contato por ID.                 |
| `GET`  | `/api/contacts/export/pdf`  | Exporta todos os contatos para PDF.       |
| `GET`  | `/api/contacts/export/excel`| Exporta todos os contatos para Excel.     |
| `POST` | `/api/contacts/import`      | Importa contatos de um arquivo CSV.       |

</details>

---

## 👨‍💻 Autor e Contato

Feito com ❤️ por **Victor Xavier**. Sinta-se à vontade para entrar em contato!

<a href="https://www.linkedin.com/in/victor-xavier-/" target="_blank">
  <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="linkedin logo"  />
</a>
<a href="mailto:victor.xavier.oliver@gmail.com">
  <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="email logo" />
</a>
<a href="https://github.com/oliver-Victorxavier" target="_blank">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="github logo" />
</a>

---

## 📜 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE) para mais detalhes.