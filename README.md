# Apache Kafka with Spring Boot

A hands-on project demonstrating Apache Kafka integration with Spring Boot, using KRaft mode (no Zookeeper).

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.5**
- **Spring Kafka**
- **Apache Kafka 7.9.0** (KRaft mode)
- **Docker**
- **Lombok**

## 📁 Project Structure

```
apache-kafka-with-spring-boot-reactive/
│
├── docker-compose.yml          # Kafka broker (KRaft mode)
│
├── kafka-demo/                 # Main Spring Boot app
│   └── src/main/java/com/abdel/kafkademo/
│       ├── config/
│       │   └── KafkaTopicConfig.java       # Topic creation
│       ├── consumer/
│       │   └── KafkaConsumer.java          # Message consumer
│       ├── producer/
│       │   ├── KafkaProducer.java          # String producer
│       │   └── KafkaJsonProducer.java      # JSON producer
│       ├── payload/
│       │   └── Student.java               # Message model
│       └── rest/
│           └── MessageController.java      # REST endpoints
│
├── consumer/                   # Standalone consumer module
└── producer/                   # Standalone producer module (Wikimedia)
```

## 🚀 Getting Started

### 1. Start Kafka with Docker

```bash
docker-compose up -d
docker logs ms_kafka   # verify Kafka is running
```

### 2. Run the Spring Boot App

```bash
cd kafka-demo
./mvnw spring-boot:run
```

### 3. Test the Endpoints

**Send a plain String message:**
```bash
curl -X POST http://localhost:8080/api/v1/messages \
  -H "Content-Type: text/plain" \
  -d "Hello Kafka!"
```

**Send a JSON message (Student object):**
```bash
curl -X POST http://localhost:8080/api/v1/messages/json \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "firstname": "Abdel", "lastname": "Test"}'
```

## ⚙️ Configuration

### `docker-compose.yml` — Kafka in KRaft mode (no Zookeeper)

```yaml
version: '3'
services:
  kafka:
    image: confluentinc/cp-kafka:7.9.0
    container_name: ms_kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: true
      CLUSTER_ID: MkU3OEVBNTcwNTJENDM2Qk
```

### `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: myGroup
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: '*'
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

## 🔄 Architecture

```
POST /api/v1/messages/json
        │
        ▼
MessageController
        │
        ▼
KafkaJsonProducer ──► Kafka Topic "abdel"
                              │
                              ▼
                      KafkaConsumer ──► Log to console
```

## 🧹 Useful Commands

```bash
# Enter Kafka container
docker exec -it ms_kafka bash

# List topics
kafka-topics --bootstrap-server localhost:9092 --list

# Delete a topic
kafka-topics --bootstrap-server localhost:9092 --delete --topic <topic-name>

# Consume messages from a topic
kafka-console-consumer --topic <topic-name> --from-beginning --bootstrap-server localhost:9092

# Reset consumer group offset
kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group myGroup --reset-offsets --to-earliest --all-topics --execute
```

## 📚 Concepts Covered

- ✅ Kafka Producer & Consumer with Spring Boot
- ✅ JSON Serialization / Deserialization
- ✅ KRaft mode (no Zookeeper)
- ✅ Topic creation with `TopicBuilder`
- ✅ Consumer Groups
- 🔜 Wikimedia real-time stream
- 🔜 Error handling & Dead Letter Topic
- 🔜 Spring WebFlux (reactive)

## 📝 License

MIT
