package com.example.config;

import com.example.helper.MessageHandler;
import com.example.model.ConsumerMessage;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    MessageHandler messageHandler;
    Map<String, Object> defaultConsumerKafkaProperties;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.StringDeserializer}")
    public String keyDeserializer;
    @Value("${spring.deserializer.delegate.class:org.apache.kafka.common.serialization.ByteArrayDeserializer}")
    public String valueDeserializerDelegate;
    @Value("${spring.value.deserializer.class:org.springframework.kafka.support.serializer.ErrorHandlingDeserializer}")
    public String valueDeserializer;
    @Value("${spring.kafka.retry.backoff.ms:50000")
    String retryBackoffMs;
    @Value("${spring.kafka.reconnect.backoff.max.ms:1000")
    String reconnectMaxBackoffMs;
    @Value("${spring.kafka.reconnect.backoff.ms:500")
    String reconnectBackoffMs;
    @Value("${spring.kafka.request.timeout.ms:35000")
    String requestTimeoutMs;
    @Value("${spring.kafka.connections.max.idle.ms:240000")
    String connectionMaxIdleMs;
    @Value("${spring.kafka.max.poll.records:500")
    String maxPollRecords;
    @Value("${spring.kafka.max.poll.interval.ms:480000")
    String maxPollInterval;
    @Value("${spring.kafka.max.partition.fetch.bytes:52428800")
    String partitionFetchBytes;
    @Value("${spring.kafka.session.timeout.ms:10000")
    String sessionTimeoutMs;
    @Value("${consumer.bootstrap.server}")
    private String bootStrapServer;
    @Value("${consumer.listener.factory.concurrency}")
    private int listenerFactoryConcurrency;
    @Value("${spring.kafka.consumer.client-id:}")
    private String clientId;
    @Value("${spring.profiles.active:local}")
    private String activeProfile;
    @Value("${backoff.initial.interval}")
    private long initialInvterval;
    @Value("${backoff.multiplier}")
    private double multiplier;
    @Value("${backoff.max.elapsed.time}")
    private long maxElapsedTime;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String autoCommitConfig;
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollReConfig;
    @Value("${spring.kafka.default.api.timeout.ms:300000")
    private String defaultApiTimeout;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConsumerMessage> consumerFactory() {
        Map<String, Object> kafkaConsumerProps = kafkaProperties.buildConsumerProperties();
        defaultConsumerKafkaProperties = defaultConsumerKafkaProperties();
        logger.info("Building the consumer Listener Container");

        ConcurrentKafkaListenerContainerFactory<String, ConsumerMessage> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        final Map<String, Object> properties = new HashMap<>();

        if (!"local".equals(activeProfile)) {
            properties.putAll(kafkaConsumerProps);
        }

        properties.putAll(defaultConsumerKafkaProperties);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ByteArrayDeserializer.class.getName());
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommitConfig);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollReConfig);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);

        // Create the consumer :)
        final ConsumerFactory<String, ConsumerMessage> consumerFactory = new DefaultKafkaConsumerFactory<>(properties);
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setCommonErrorHandler(errorHandler());
        kafkaListenerContainerFactory.setConcurrency(listenerFactoryConcurrency);

        logger.info("Listener container created!");
        return kafkaListenerContainerFactory;

    }

    @Bean
    public CommonErrorHandler errorHandler() {

        ExponentialBackOff backOff = new ExponentialBackOff(initialInvterval, multiplier);
        backOff.setMaxElapsedTime(maxElapsedTime);
        return new DefaultErrorHandler((consumerRecord, e) -> {
            String data = new String((byte[]) consumerRecord.value());

            if (consumerRecord.value() != null) {
                try {
                    messageHandler.processMessage(consumerRecord.offset(), String.valueOf(consumerRecord.partition()), data);

                } catch (Exception ex) {

                }
            }
            logger.error("Invalid Payload. Event Details: partition: {}, offset: {}, topic: {}, key: {}, value: {}",
                    consumerRecord.partition(), consumerRecord.offset(), consumerRecord.topic(), consumerRecord.key(),
                    data);
        }, backOff);

    }

    public Map<String, Object> defaultConsumerKafkaProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, reconnectMaxBackoffMs);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, reconnectBackoffMs);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, connectionMaxIdleMs);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, partitionFetchBytes);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, defaultApiTimeout);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);

        return properties;
    }


}
