# AegisStream

> Real-Time Distributed Fraud Mitigation Engine

## Problem Statement

In modern digital banking, transactions occur at a rate of thousands per second. Monolithic legacy systems face critical failures:
- **Latency**: Synchronous validation slows down the user experience.
- **Scalability**: If the fraud detection logic becomes complex, it slows down the entire banking core.
- **Data Silos**: Fraud patterns often emerge across different regions, but data isn't synced in real-time.

## The Solution

**AegisStream** is a cloud-native, event-driven microservices ecosystem. It decouples the "Action" (Processing a payment) from the "Validation" (Checking for fraud). By using an **Asynchronous Event Mesh**, the system can process payments instantly while a secondary "Brain" analyzes the transaction for risks and triggers automated account freezes or notifications within milliseconds.

## Tech Stack

- **Backend**: Java 21 (utilizing Virtual Threads/Project Loom for high-throughput I/O).
- **Framework**: Spring Boot 3.4+, Spring Cloud (Gateway, Config, Eureka).
- **Message Broker**: Apache Kafka (The central nervous system for event streaming).
- **Persistence**: * PostgreSQL: For immutable transaction records.
    - **Redis**: For "Hot Data" (e.g., checking if a user’s card was used in two different cities in 10 minutes).
- **Security**: Keycloak (OAuth2/OpenID Connect) for secure service-to-service communication.
- **Monitoring**: Micrometer + Prometheus + Grafana (The "Golden Signals" of SRE).

## Project Flow and Architecture

1. **Ingress**: A mobile app sends a JSON transaction to the Spring Cloud Gateway.
2. **Authentication**: The Gateway validates the JWT via Keycloak.
3. **Command Phase**: The Transaction Service saves the "Pending" transaction to PostgreSQL and immediately publishes a TransactionCreated event to the Kafka "tx-topic".
4. **Analysis Phase**: The Fraud Service (a Kafka Consumer) picks up the event. It runs a rules engine check (e.g., comparing the amount against the user's average via Redis).
5. **Reaction Phase**: 
    - If Clean: No further action (or update status to "Completed").
    - If Fraud: It publishes a FraudAlert event to a separate topic.
6. **Notification Phase**: The Notification Service consumes the alert and pushes a real-time message to the user via WebSockets.

## Directory Structure

```md
aegis-stream/                  (Root Directory)
├── docker-compose.yml         (Infrastructure: Kafka, Postgres, Redis)
├── pom.xml                    (Parent POM: Manages shared dependencies/versions)
│
├── api-gateway/               (Spring Cloud Gateway)
│   ├── src/main/java/...      (Routing & Security logic)
│   └── src/main/resources/    (application.yml with routes)
│
├── transaction-service/       (The Producer)
│   ├── src/main/java/         
│   │   ├── com.aegis.tx.controller/  (REST Endpoints)
│   │   ├── com.aegis.tx.service/     (Business Logic)
│   │   ├── com.aegis.tx.model/       (PostgreSQL Entities)
│   │   └── com.aegis.tx.producer/    (Kafka logic)
│   ├── src/test/java/                (JUnit & Testcontainers)
│   └── src/main/resources/           (DB & Kafka configs)
│
├── fraud-service/             (The Consumer/Brain)
│   ├── src/main/java/         
│   │   ├── com.aegis.fraud.listener/ (Kafka @KafkaListener)
│   │   ├── com.aegis.fraud.rules/    (Fraud logic/Drools)
│   │   └── com.aegis.fraud.cache/    (Redis integration)
│   └── src/main/resources/           (Redis & Kafka configs)
│
├── notification-service/      (The Messenger)
│   ├── src/main/java/         
│   │   ├── com.aegis.notif.listener/ (Listens to "fraud-alerts")
│   │   └── com.aegis.notif.provider/ (Email/SMS/WebSocket logic)
│   └── src/main/resources/
│
├── common-library/            (Shared DTOs and Utils)
│   └── src/main/java/         (TransactionEvent.java shared by all)
│
└── observability/             (Monitoring Configs)
    ├── prometheus.yml
    └── grafana-dashboards/
```

> TODO: Create notion plan page, and start the project.