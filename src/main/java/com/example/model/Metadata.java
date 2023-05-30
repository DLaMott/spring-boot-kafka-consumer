package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metadata {
    @JsonProperty("dataClassification")
    public String dataClassification;
    @JsonProperty("eventDateTime")
    public String eventDateTime;
    @JsonProperty("eventId")
    public String eventId;
    @JsonProperty("topicName")
    public String topicName;
    @JsonProperty("eventName")
    public String eventName;
    @JsonProperty("version")
    public String version;

    @Override
    public String toString() {
        return "MetaData{" +
                "dataClassification='" + dataClassification + '\'' +
                ", eventDateTime='" + eventDateTime + '\'' +
                ", eventId='" + eventId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", eventName='" + eventName + '\'' +
                ", version='" + version + '\'' +
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
