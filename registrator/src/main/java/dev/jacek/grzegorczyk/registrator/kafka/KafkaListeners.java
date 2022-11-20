package dev.jacek.grzegorczyk.registrator.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "coordinator.coordinator.api_message",
            groupId = "group_id")
    void listener(String data) {
        System.out.println("LISTENER RECEIVED: " + data);
    }
}
