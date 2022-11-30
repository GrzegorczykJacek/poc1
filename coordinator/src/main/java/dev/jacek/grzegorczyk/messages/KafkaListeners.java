package dev.jacek.grzegorczyk.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.service.ApiMessageService;
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
    private final ApiMessageService apiMessageService;

    @KafkaListener(topics = "outbox.event.Registration",
            groupId = "coordinator_group_id")
    void listener(String data) {

        try {
            System.out.println("LISTENER RECEIVED: " + data);
            KafkaMessage kafkaMessage = objectMapper.readValue(data, new TypeReference<>() {
            });
            Payload payload = kafkaMessage.getPayload();
            String registrationPayloadMessage = payload.getPayload();
            RegistrationPayload registrationPayload = objectMapper.readValue(registrationPayloadMessage, new TypeReference<>() {
            });
            log.info("KAFKA MESSAGE DESERIALIZED: {}", registrationPayload);

            String contents = registrationPayload.getContents();
            ApiMessage apiMessage = objectMapper.readValue(contents, new TypeReference<>() {
            });
            apiMessageService.setIsRegistered(apiMessage.getId());

            kafkaTemplate.send("registered-messages", "1", contents);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
