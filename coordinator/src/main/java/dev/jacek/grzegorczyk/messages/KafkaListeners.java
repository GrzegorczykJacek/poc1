package dev.jacek.grzegorczyk.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "registrator.registrator.registration",
            groupId = "coordinator_group_id")
    void listener(String data) {

        try {
            System.out.println("LISTENER RECEIVED: " + data);
            KafkaMessage kafkaMessage = objectMapper.readValue(data, new TypeReference<>() {
            });
            Payload payload = kafkaMessage.getPayload();
            long id = payload.getAfter().getId();
            String contents = payload.getAfter().getContents();
            log.info("KAFKA MESSAGE DESERIALIZED: id: {}, contents: {}", id, contents);

            kafkaTemplate.send("registered-messages", "1",  contents);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
