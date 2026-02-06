# Projeto - Artists & Albums API

### REST API para gerenciamento de Artistas, Álbuns, Músicas e Capas.

Esta API foi desenvolvida como parte do processo seletivo para **Engenheiro de Computação** realizado pela **SEPLAG**. O projeto demonstra a aplicação prática de requisitos técnicos de alto nível, com foco em escalabilidade, segurança e boas práticas de arquitetura.
Link com alguns detalhes do projeto: [https://drive.google.com/file/d/1tFU9LdoP5ODQCzQ3WfwBz8pqwgO_8VnL/view?usp=sharing](https://drive.google.com/file/d/1tFU9LdoP5ODQCzQ3WfwBz8pqwgO_8VnL/view?usp=sharing)
---

## 🚀 Sobre o Projeto

O backend foi construído utilizando **Java 17 + Spring Boot**, adotando uma arquitetura em camadas e divisão por funcionalidades (features). A persistência utiliza PostgreSQL com migrações versionadas, e o armazenamento de mídias é feito de forma desacoplada via protocolo S3 (MinIO).

### Diferenciais Implementados:
- **Arquitetura:** Camadas (Controller → Service → Repository) com foco em domínio/feature.
- **Segurança:** Autenticação Stateless com JWT e suporte a Refresh Token.
- **Upload de Arquivos:** Integração com Storage S3-compatible (MinIO).
- **Infraestrutura:** Ambiente totalmente dockerizado.
- **Qualidade:** Testes automatizados e documentação interativa via Swagger.

---

## 🛠️ Stack Tecnológica

| Categoria | Tecnologia |
|-----------|-----------|
| **Backend** | Java 17, Spring Boot 3 |
| **Segurança** | Spring Security + JWT |
| **Banco de Dados** | PostgreSQL |
| **ORM / Migrations** | Spring Data JPA (Hibernate) / Flyway |
| **Storage** | MinIO (S3 compatible) |
| **Documentação** | Swagger / OpenAPI 3 |
| **Testes** | JUnit 5 + Mockito |
| **Containerização** | Docker / Docker Compose |

---

## 🏗️ Arquitetura e Fluxo

A aplicação segue o fluxo clássico de responsabilidades:
`Controller → Service → Repository → Database`

Para mídias:
`Controller → Service → Storage Service → S3 (MinIO)`

### Responsabilidades por Camada
* **Controller:** Gerenciamento de protocolos HTTP, mapeamento de DTOs e validação de entrada.
* **Service:** Implementação das regras de negócio e orquestração de processos.
* **Repository:** Abstração da camada de persistência.
* **Security:** Filtros de autenticação, autorização e geração/validação de tokens.
* **Storage:** Lógica de integração para upload/download de mídias no S3.

---

## ✅ Status do projeto

- [x] Autenticação (JWT + Refresh Token)
- [x] CRUD de Artistas
- [x] CRUD de Álbuns
- [x] Upload de capa de álbum (MinIO)
- [x] Link pré-assinado para download (30 min)
- [x] Cadastro de músicas (batch) e consulta por álbum
- [x] Paginação nas consultas
- [x] Rate limit por usuário

## ⚙️ Executando o Projeto

### 1. Subir a Infraestrutura (Docker)
Certifique-se de ter o Docker instalado e execute:
docker compose up -d

Serviços disponíveis:

* Postgres: localhost:5432 (user/pass: postgres/postgres)
* MinIO API: http://localhost:9000 (admin/admin123)
* MinIO Console: http://localhost:9001
