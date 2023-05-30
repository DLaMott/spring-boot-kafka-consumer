package com.example.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.helper.ConsumerHelper;
import com.example.helper.MessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class KafkaConsumerServiceTest {

    @InjectMocks
    KafkaConsumerService kafkaConsumerService;
    @Mock
    MessageHandler messageHandler;

    @Mock
    ConsumerHelper consumerHelper;

    private String kafkaMessage;

    @Test
    void testMessageConsumptionPass() {

        createValidMethod();

        Logger testLogger = (Logger) LoggerFactory.getLogger(kafkaConsumerService.getClass());
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        testLogger.addAppender(listAppender);
        long offset = 987987;

        when(consumerHelper.isValidMessage(kafkaMessage)).thenReturn(true);

        kafkaConsumerService.consume(offset, "1", kafkaMessage);
        messageHandler.processMessage(offset, "1", kafkaMessage);
        List<ILoggingEvent> eventList = listAppender.list;

        assertEquals("[INFO] Consumer offset: 987987, Current partition: 1, Message: " +
                "{\"data\":{\"operation\":\"update\",\"eventKey\":\"JohnSmith\"},\"metadata\":" +
                "{\"dataClassification\":\"public\",\"eventDateTime\":\"2023-12-21T05:05:15.65445Z\"" +
                ",\"eventId\":\"23434-343-3434-33\",\"topicName\":\"test-topic\", \"eventName\":\"fire-topic\"" +
                ", \"version\":\"v1\"}}", eventList.get(0).toString());
    }

    @Test
    void testMessageConsumptionFail() {

        createInvalidMethod();

        Logger testLogger = (Logger) LoggerFactory.getLogger(kafkaConsumerService.getClass());
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        testLogger.addAppender(listAppender);
        long offset = 987987;

        kafkaConsumerService.consume(offset, "1", kafkaMessage);
        messageHandler.processMessage(offset, "1", kafkaMessage);
        List<ILoggingEvent> eventList = listAppender.list;

        assertEquals("[ERROR] Invalid Payload!, Consumer offset: 987987, Current partition: 1, Message: " +
                        "{\"dataaa\":{\"operation\":\"update\",\"eventKey\":\"JohnSmith\"},\"metadata\":{\"dataClassification\"" +
                        ":\"public\",\"eventDateTime\":\"2023-12-21T05:05:15.65445Z\",\"eventId\":\"23434-343-3434-33\"," +
                        "\"topicName\":\"test-topic\", \"eventName\":\"fire-topic\", \"version\":\"v1\"}}"
                , eventList.get(1).toString());
    }

    private void createValidMethod() {
        kafkaMessage = "{\"data\":{\"operation\":\"update\",\"eventKey\":\"JohnSmith\"},\"metadata\":" +
                "{\"dataClassification\":\"public\",\"eventDateTime\":\"2023-12-21T05:05:15.65445Z\"," +
                "\"eventId\":\"23434-343-3434-33\",\"topicName\":\"test-topic\", \"eventName\":\"fire-topic\"," +
                " \"version\":\"v1\"}}";
    }

    private void createInvalidMethod() {
        kafkaMessage = "{\"dataaa\":{\"operation\":\"update\",\"eventKey\":\"JohnSmith\"},\"metadata\":" +
                "{\"dataClassification\":\"public\",\"eventDateTime\":\"2023-12-21T05:05:15.65445Z\"," +
                "\"eventId\":\"23434-343-3434-33\",\"topicName\":\"test-topic\", \"eventName\":\"fire-topic\"," +
                " \"version\":\"v1\"}}";
    }


}
