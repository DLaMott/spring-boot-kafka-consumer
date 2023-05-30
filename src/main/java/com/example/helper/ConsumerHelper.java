package com.example.helper;

import com.example.model.ConsumerMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsumerHelper {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean isValidMessage(String kafkaMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(kafkaMessage, ConsumerMessage.class);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
