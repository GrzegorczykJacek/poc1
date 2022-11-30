package dev.jacek.grzegorczyk.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {

    @KafkaListener(topics = "registered-messages",
            groupId = "notifier_group_id")
    void listener(String data) {
        log.info("LISTENER RECEIVED: {}", data);
    }
}
