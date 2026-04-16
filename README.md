# Ágora — Community Blog Platform · Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" />
</p>

> **Portfolio repository.** This is the public showcase version of the Ágora backend.  
> Credentials, secrets and production endpoints are managed via environment variables and are not stored here.

---

## What is Ágora?

Ágora is a full-stack community blog platform where users can publish posts, comment, reply, and interact under a moderated environment. The backend is a production-grade REST API built with Spring Boot 3 and Java 25, featuring an event-driven content moderation pipeline, GDPR compliance, and multi-channel notification delivery.

This repository covers **the entire backend**: authentication, content lifecycle, automated NLP moderation via Apache Kafka, SMTP email notifications, GDPR right-to-erasure, and a full audit trail.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                        REST API (HTTPS)                      │
│              Spring Security · JWT · OAuth2 (Google)         │
└──────────────────────────┬──────────────────────────────────┘
                           │
          ┌────────────────▼────────────────┐
          │           Service Layer          │
          │   Posts · Comments · Replies     │
          │   Users · Profiles · Settings    │
          └────────────┬───────────┬─────────┘
                       │           │
          ┌────────────▼──┐  ┌─────▼──────────────────┐
          │  MySQL / H2   │  │    Apache Kafka         │
          │  (JPA + ORM)  │  │                         │
          └───────────────┘  │  topic: "comments"      │
                             │  topic: "replies"        │
                             └─────────┬───────────────┘
                                       │
                          ┌────────────▼──────────────┐
                          │   Moderation Pipeline       │
                          │  NLP Sentiment Analysis     │
                          │  (custom + OpenNLP model)   │
                          └────────────┬───────────────┘
                                       │
                     ┌─────────────────▼──────────────────┐
                     │       Notification Dispatch          │
                     │  ┌──────────────────────────────┐  │
                     │  │  JavaMailSender (SMTP / SES)  │  │
                     │  │  Push Notifications           │  │
                     │  └──────────────────────────────┘  │
                     │  → Author: approved / rejected      │
                     │  → Admin:  review queue alert       │
                     │  → Replier's target: new reply!     │
                     └────────────────────────────────────┘
```

---

## ✨ Key Features

### Authentication & Security
- **JWT authentication** with refresh-token support (Spring Security 6)
- **OAuth2 / Google Sign-In** — server-side token verification via `GoogleIdTokenVerifier`
- **Role-based access control** — `ADMIN`, `USER`, `MODERATOR` roles
- **BCrypt** password hashing, brute-force mitigation

### Content & Community
- **Posts, Comments, Replies** — full CRUD with pagination
- **Hybrid avatar system** — preloaded system avatars + custom user uploads (stored in filesystem / S3-ready)
- **User profiles & settings** — bio, social links, notification preferences
- **Tags & categories** for post organisation
- **Legal text management** — dynamic terms of service, privacy policy (admin-editable)

### Event-Driven Moderation Pipeline
- **Apache Kafka** topics: `comments`, `replies`
- **NLP sentiment analysis** (custom multilingual classifier + OpenNLP model) — auto-detects offensive language
- **Dual outcome dispatch** per Kafka event:
  - _Censored_ → email author with rejection reason + alert admin for manual override
  - _Approved_ → email author confirmation + alert admin (new content) + push notification
- **Reply chain notification** — the original commenter is notified when someone replies

### Notifications
- **Email** via `JavaMailSender` (dev: WARN if SMTP unconfigured; prod: AWS SES)
- **Push notifications** — server-side push dispatch
- **Admin review queue** — auto-moderated content flagged to admin with full context

### GDPR & Compliance
- **Right to erasure** (`GdprService`) — cascading deletion across all user content and associated data
- **Data export** endpoint — users can download their data
- **Audit trail** — full event log for all sensitive operations
- **Legal text versioning** — each legal document update is tracked

### Developer Experience
- **OpenAPI / Swagger UI** at `/swagger-ui.html`
- **Multi-environment config**: `dev` (H2), `mysql`, `staging`, `prod`
- **Docker + docker-compose** for local infrastructure (Kafka + MySQL)
- **`kafka.enabled`** flag — run the full stack without Kafka locally

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 3.5.13 |
| Security | Spring Security 6, JWT, OAuth2 (Google) |
| Persistence | Spring Data JPA, Hibernate, MySQL 8 / H2 |
| Messaging | Apache Kafka (Spring Kafka) |
| NLP | Custom sentiment classifier + Apache OpenNLP model |
| Email | Spring Mail → JavaMailSender (AWS SES in prod) |
| API Docs | Springdoc OpenAPI 2.8 (Swagger UI) |
| Build | Maven, Java 25 |
| Containers | Docker, docker-compose |
| Testing | JUnit 5, Mockito, Spring Boot Test |

---

## 🚀 Quick Start

### Prerequisites

- **Java 25** — [Download JDK 25](https://jdk.java.net/25/) and set `JAVA_HOME`
- **Maven 3.9+** — included as `./mvnw` wrapper, no install needed
- **RSA key pair** — required for JWT signing (one-time setup, see below)
- **Docker Desktop** — only needed for the full stack (MySQL + Kafka)

### 1. Generate RSA keys (first time only)

JWT signing requires a key pair. Run once from the project root:

```bash
# Windows (PowerShell)
mkdir src\main\java\de\stella\agora_web\auth\keys
cd src\main\java\de\stella\agora_web\auth\keys

openssl genrsa -out access-token-private.key 2048
openssl rsa -in access-token-private.key -pubout -out access-token-public.key
openssl genrsa -out refresh-token-private.key 2048
openssl rsa -in refresh-token-private.key -pubout -out refresh-token-public.key
```

> These files are in `.gitignore` — they are never committed.

### 2. Run in dev mode — H2 in-memory DB, no Kafka needed

```bash
git clone https://github.com/Stegonyrob/agora_back.git
cd agora_back
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

- App: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, pass: `password`)

**The dev profile seeds the database automatically with test users:**

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `user1` | `user1` | USER |
| `user2` | `user2` | USER |
| `user3` | `user3` | USER |

> Passwords are BCrypt-hashed in `data.sql`. These users only exist in the H2 in-memory database and are reset on every restart.

### 3. Run with Docker — full stack (MySQL + Kafka)

```bash
docker-compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

> Copy `application-mysql.properties` and set your local DB credentials, or set the env vars below.

### 4. Environment variables (production)

| Variable | Description | Default (dev) |
|---|---|---|
| `DATABASE_URL` | `jdbc:mysql://host:3306/agora_db` | H2 in-memory |
| `DATABASE_USER` | DB username | `sa` |
| `DATABASE_PASSWORD` | DB password | `password` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka broker | `localhost:9092` |
| `ADMIN_EMAIL` | Receives moderation alerts | `admin@agora.es` |
| `SES_SMTP_USERNAME` | AWS SES SMTP username | _(optional in dev)_ |
| `SES_SMTP_PASSWORD` | AWS SES SMTP password | _(optional in dev)_ |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID | _(optional in dev)_ |

> In dev mode all optional vars have safe defaults — the app starts and works without them.

---

## 📋 Main API Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Register with email/password |
| `POST` | `/api/v1/auth/login` | Login — returns JWT |
| `POST` | `/api/v1/auth/google` | Google OAuth2 token exchange |
| `GET` | `/api/v1/posts` | List posts (public, paginated) |
| `POST` | `/api/v1/posts` | Create post (authenticated) |
| `POST` | `/api/v1/comments` | Comment on a post → triggers moderation pipeline |
| `POST` | `/api/v1/replies` | Reply to a comment → triggers moderation pipeline |
| `GET` | `/api/v1/profiles/{id}` | Get user profile |
| `PUT` | `/api/v1/profiles/{id}` | Update profile + avatar |
| `GET` | `/api/v1/avatars` | List available avatars |
| `POST` | `/api/v1/gdpr/delete` | GDPR right-to-erasure request |
| `GET` | `/api/v1/gdpr/export` | Export own data |
| `GET` | `/api/v1/admin/moderation` | Moderation queue (ADMIN only) |
| `GET` | `/api/v1/legal-texts/{type}` | Get legal document by type |
| `GET` | `/swagger-ui.html` | Full interactive API docs |

---

## 🧪 Testing

```bash
# All tests
./mvnw test

# Specific suite
./mvnw test -Dtest="*CommentServiceTest"
./mvnw test -Dtest="*ModerationTest"
```

---

## 🔒 Security Notes (Portfolio Repo)

This is the **public showcase version**. The following are intentionally absent:

- No real credentials anywhere (all via env vars)
- `client_secret.json` referenced in `.gitignore` — must be provided at deploy time
- RSA keys for JWT signing loaded from env, not committed
- CORS origins set via `CORS_ALLOWED_ORIGINS` env var

For production deployment see `application-prod.properties` which references all secrets as `${ENV_VAR}`.

---

## 📁 Project Structure

```
src/main/java/de/stella/agora_web/
├── auth/           # JWT + OAuth2 (Google) authentication
├── comment/        # Comments: service, Kafka producer/consumer, DTO, NLP pipeline
├── replies/        # Replies: same pipeline as comments
├── moderation/     # NLP sentiment analysis + moderation service
├── posts/          # Post CRUD + tagging
├── user/           # User entity, registration, management
├── profiles/       # User profiles + avatar management
├── avatar/         # Avatar upload and system avatars
├── notification/   # Push notification dispatch
├── gdpr/           # Right-to-erasure + data export
├── audit/          # Audit trail for sensitive operations
├── security/       # SecurityConfig, JWT filter chain
├── config/         # Kafka, Mail, CORS, OpenAPI config
├── legal_text/     # Dynamic legal document management
├── export/         # Data export service
└── exception/      # Global exception handler (RFC 7807 Problem Details)
```

---

## 🗺️ Roadmap / Production Checklist

- [x] Event-driven moderation pipeline (Kafka)
- [x] NLP auto-moderation with admin override alerts
- [x] Multi-scenario email notifications (SMTP / AWS SES)
- [x] GDPR right to erasure
- [x] Google OAuth2 sign-in
- [x] OpenAPI documentation
- [x] Docker compose for local dev
- [ ] Frontend integration (separate repo)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Horizontal Kafka consumer scaling
- [ ] Redis caching layer

---

## 📄 License

MIT © Ágora Project — see [LICENSE](LICENSE) for details.

