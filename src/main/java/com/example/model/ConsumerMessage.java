package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerMessage {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("metadata")
    private Metadata metadata;

    @Override
    public String toString() {
        return "ConsumerMessage{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }

    /*
     * Example payload:
     * {"data":{"operation":"update","eventKey":"JohnSmith"},"metadata":{"dataClassification":"public","eventDateTime":
     * "2023-12-21T05:05:15.65445Z","eventId":"23434-343-3434-33","topicName":"test-topic", "eventName":
     * "fire-topic", "version":"v1"}}
     *
     *
     * */
}
