package com.example.service;

import com.example.helper.ConsumerHelper;
import com.example.helper.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ConsumerHelper consumerHelper;
    @Autowired
    MessageHandler messageHandler;

    public void consume(@Header(KafkaHeaders.OFFSET) long offset, @Header(KafkaHeaders.RECEIVED_PARTITION) String
            parition, String message) {
        logger.info("Consumer offset: {}, Current partition: {}, Message: {}", offset, parition, message);

        if (consumerHelper.isValidMessage(message)) {
            messageHandler.processMessage(offset, parition, message);
        } else {
            logger.error("Invalid Payload!, Consumer offset: {}, Current partition: {}, Message: {}"
                    , offset, parition, message);
        }
    }
}
