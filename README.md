# Websocket API Demo

This is a simple demo showing how to configure websockets in Spring Boot. It uses the `spring-boot-starter-websocket` dependency to enable websocket support in the application. It allows any topic under the `/private` prefix to be subscribed to by any client. The client can then send messages to the server, which will be broadcast to all clients subscribed to the same topic.

There are two endpoints exposed by the server: `/automation` and `/kafka/automation`. These endpoints are used to simulate triggering an automation event. When a client sends a message to this endpoint, the server will broadcast a message to all clients subscribed to the topic key that was passed in the body of the request. This service method simulates long-running tasks with `Thread.sleep()` and will use websockets to notify the client after each task is complete.

In order to use the `/kafka/automation` endpoint, you need to have kafka running on your local machine. You can find the instructions on how to start kafka in the [Kafka profile](#kafka-profile) section.

## Pre-requisites
- Java 17
- Docker

To build the project, run the following command:
```shell
./gradlew build
```

The server runs on port `8080` and can be accessed at `http://localhost:8080`.
The server can be started with the following command:
```shell
./gradlew bootRun
```

## Customizing the Server
The server can be customized by modifying the `application.properties` file, located under `src/main/resources/`. The following properties can be set:
- `server.port`: The port the server will run on. Default is `8080`. Can be overridden by setting the `SERVER_PORT` environment variable.
- `websocket.allowed-origins`: The allowed origins for websocket connections. Default is `http://localhost:3000`. Can be overridden by setting the `WEBSOCKET_ALLOWED_ORIGINS` environment variable.

## Kafka profile

To use kafka profile, you need to have kafka running on your local machine. You can start kafka using the following command, located in the `local` directory:
```shell
docker compose up -d
```

To simulate multiple pods, you can run the following command in the `local` directory:
```shell
./run.sh .envrc.pod1
```
Open a new terminal and run the following command in the `local` directory:
```shell
./run.sh .envrc.pod2
```

#### Kafka properties
The following properties can be set in the `application-kafka.properties` file, located under `src/main/resources/`:
- `kafka.topic`: The topic to subscribe to. Default is `test.topic`. Can be overridden by setting the `KAFKA_TOPIC` environment variable.
- `kafka.group-id`: The group id for the kafka consumer. Default is `test-group`. Can be overridden by setting the `KAFKA_GROUP_ID` environment variable.
- `spring.kafka.bootstrap-servers`: The kafka server to connect to. Default is `localhost:9092`.
- `spring.kafka.properties.security,protocol`: The security protocol to use. Default is `PLAINTEXT`.
