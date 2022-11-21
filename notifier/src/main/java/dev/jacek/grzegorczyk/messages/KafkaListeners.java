package dev.jacek.grzegorczyk.messages;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "coordinator.coordinator.api_message",
            groupId = "new_group_id")
    void listener(String data) {
        System.out.println("LISTENER RECEIVED: " + data);
    }
}
