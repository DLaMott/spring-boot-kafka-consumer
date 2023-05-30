# Spring Boot KafkaConsumer

## Features

* Spring-Boot kafka consumer showcase
* JUnit
* Localstack configuration
* Gradle implementation

## Don't want the code?

Use my kafka consumer generator!
*Requires Node/ NPM to be set up*

```
> npm install -g yo
> npm install -g generator-spring-boot-kafka-consumer
> yo generator-spring-boot-kafka-consumer
```

## Local Development Setup:
***Steps To test Consumer against a Producer***

1. **Ensure Kafka is installed on your local machine:**
   - *Recommended Installation Guide:* https://hevodata.com/learn/install-kafka-on-windows/

2. **Start Kafka!**
- .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties to start zookeeper
- .\bin\windows\kafka-server-start.bat .\config\server.properties to start kafka server

3. **Create Topic**:
- .\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test-topic

- *View Topics*:
  - \.bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

4. **Produce Topic**:
- .\bin\windows\kafka-console-producer -broker-list localhost:9092 -topic test-topic

- *Sample Payload*:
  - {"data":{"operation":"update","eventKey":"JohnSmith"},"metadata":{"dataClassification":"public","eventDateTime":"2023-12-21T05:05:15.65445Z","eventId":"23434-343-3434-33","topicName":"test-topic", "eventName":"fire-topic", "version":"v1"}}
