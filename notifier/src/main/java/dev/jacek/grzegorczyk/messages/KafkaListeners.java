package dev.jacek.grzegorczyk.messages;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "registered-messages",
            groupId = "notifier_group_id")
    void listener(String data) {
        System.out.println("LISTENER RECEIVED: " + data);
    }
}
