# Artists & Albums API

API REST para gerenciamento de artistas, álbuns, faixas e capas de álbum, com autenticação JWT, refresh token, upload em storage S3 (MinIO) e notificações WebSocket.

## ✨ Funcionalidades

- Autenticação com **JWT** e **refresh token**.
- Cadastro e manutenção de **artistas**.
- Cadastro e manutenção de **álbuns** com associação a artistas.
- Cadastro em lote e listagem de **faixas por álbum**.
- Upload de **capa de álbum** em storage S3-compatible (MinIO).
- Geração de URL **pré-assinada** para download da capa.
- **Rate limit** para login e para chamadas autenticadas.
- Notificação em tempo real via **WebSocket/STOMP** quando álbum é criado.
- Documentação interativa com **Swagger/OpenAPI**.

---

## 🧱 Stack

- Java 17
- Spring Boot 4
- Spring Security (JWT)
- Spring Data JPA + Hibernate
- Flyway
- PostgreSQL
- MinIO (S3-compatible)
- Spring WebSocket (STOMP)
- JUnit 5 + Mockito
- Docker / Docker Compose

---

## 📁 Estrutura resumida

A aplicação segue arquitetura em camadas por feature:

- `controller`: endpoints HTTP
- `service`: regras de negócio
- `repository`: acesso a dados
- `domain`: entidades de negócio
- `dto`: contratos de entrada/saída
- `security`: autenticação/autorização e filtros

---

## ▶️ Como executar

### Pré-requisitos

- Docker e Docker Compose
- (Opcional) Java 17 + Maven para execução local fora de containers

### 1) Subir toda a stack via Docker

```bash
docker compose up -d --build
```

Serviços expostos:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`
- MinIO API: `http://localhost:9000`
- MinIO Console: `http://localhost:9001`

### 2) Variáveis de ambiente esperadas

O `docker-compose.yml` lê as variáveis de `.env`:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=appdb

MINIO_USER=admin
MINIO_PASS=admin123

JWT_SECRET=troque-por-um-segredo-forte
JWT_EXP_MINUTES=5
```

> O bucket S3 padrão usado pela API é `app-images`.

### 3) Rodar localmente (sem container da API)

Com PostgreSQL e MinIO já disponíveis:

```bash
./mvnw spring-boot:run
```

---

## 📚 Documentação da API

Com a aplicação no ar:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Base path da API:

- `/api/v1`

---

## 🔐 Autenticação e perfis

### Endpoints de auth

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`

### Usuários iniciais (migração)

Senha padrão para todos os usuários abaixo: `123456`

- `administrador` → role `ADMIN`
- `usuario_gestor` → role `MANAGER`
- `usuario_editor` → role `EDITOR`
- `usuario_1` → role `USER`
- `usuario_2` → role `USER`

### Regras de autorização (resumo)

- Artistas:
  - Criar e atualizar: `ADMIN`
  - Consultas: autenticado
- Álbuns:
  - Criar/atualizar: `ADMIN` ou `MANAGER`
  - Buscar por ID: `ADMIN`, `MANAGER` ou `EDITOR`
  - Listagens e buscas: autenticado
- Tracks e mídia:
  - Endpoints autenticados

Use o token JWT no header:

```http
Authorization: Bearer <seu_token>
```

---

## 🧪 Endpoints principais

### Artists

- `POST /api/v1/artists`
- `GET /api/v1/artists`
- `GET /api/v1/artists/search?name=`
- `GET /api/v1/artists/{id}`
- `PUT /api/v1/artists/{id}`

### Albums

- `POST /api/v1/albums`
- `GET /api/v1/albums`
- `GET /api/v1/albums/{id}`
- `PUT /api/v1/albums/{id}`
- `GET /api/v1/albums/by-title/{title}`
- `GET /api/v1/albums/by-artist?name=`

### Tracks (por álbum)

- `POST /api/v1/albums/{albumId}/tracks/batch`
- `GET /api/v1/albums/{albumId}/tracks`

### Mídia de capa

- `POST /api/v1/media/{albumId}/cover` (multipart/form-data)
- `GET /api/v1/media/{albumId}/cover`

---

## 📡 WebSocket

- Endpoint STOMP: `/ws`
- Tópico de publicação de novo álbum: `/topic/albums/created`

---

## 🩺 Healthcheck

- `GET /actuator/health`
- `GET /actuator/info`

---

## ✅ Testes

Executar testes automatizados:

```bash
./mvnw test
```

---

## Observações

- As migrações Flyway são aplicadas na inicialização.
- O bucket S3 é verificado/criado automaticamente na subida da aplicação.
- Projeto desenvolvido no contexto de avaliação técnica (SEPLAG).
