package com.example.helper;

import com.example.model.ConsumerMessage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void processMessage(long offset, String partition, String kafkaMessage){

        ConsumerMessage consumerMessage = new Gson().fromJson(kafkaMessage, ConsumerMessage.class);

        logger.info(consumerMessage.toString());

    }
}
