# Ekko Chat

Distributed chat application built with Java, Spring Boot, gRPC, RabbitMQ and JWT authentication.

## Architecture

Ekko is split into independent microservices communicating via REST, gRPC and a message queue.

### Services

| Service | Port | Description |
|---|---|---|
| ekko-bff | 8080 | Backend-for-Frontend, REST API gateway with JWT validation |
| ekko-auth | 8081 | Authentication, issues JWT tokens |
| ekko-user | 8082 | User profiles and gRPC endpoint |
| ekko-message | 8083 | Messages, publishes events to RabbitMQ |
| ekko-bot | 8084 | Consumes message events and auto-replies |

### Infrastructure

| Component | Port | Description |
|---|---|---|
| RabbitMQ | 5672 | Message queue / event bus |
| PostgreSQL (user) | 5433 | Database for User Service |
| PostgreSQL (message) | 5434 | Database for Message Service |
| PostgreSQL (auth) | 5435 | Database for Auth Service |

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT
- gRPC (spring-grpc)
- RabbitMQ + Spring AMQP
- PostgreSQL + Spring Data JPA
- Maven (multi-module)
- Docker + Docker Compose

## Getting Started

```bash
docker-compose up
```

## Communication

- **Client → BFF**: REST
- **BFF → Auth/User/Message**: REST
- **Message → User**: gRPC
- **Message → RabbitMQ**: publish `message-published`
- **Bot → RabbitMQ**: consume `message-published`
