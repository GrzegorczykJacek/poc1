package dev.jacek.grzegorczyk.registrator.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.repo.RegistrationRepo;
import dev.jacek.grzegorczyk.registrator.services.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static dev.jacek.grzegorczyk.registrator.enums.OutboxOperation.REGISTRATION_CREATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final ObjectMapper objectMapper;
    private final RegistrationRepo registrationRepo;
    private final OutboxService outboxService;

    @KafkaListener(topics = "coordinator.coordinator.api_message",
            groupId = "registrator_group_id")
    void listener(String data) {

        try {
            System.out.println("LISTENER RECEIVED: " + data);
            KafkaMessage kafkaMessage = objectMapper.readValue(data, new TypeReference<>() {
            });
            Payload payload = kafkaMessage.getPayload();
            String author = payload.getAfter().getAuthor();
            String message = payload.getAfter().getMessage();
            long id = payload.getAfter().getId();
            log.info("KAFKA MESSAGE DESERIALIZED: id: {}, author: {}, message: {}", id, author, message);

            Registration registration = new Registration();
            registration.setContents("Id: " + id + " author: " + author + " message: " + message);

            registrationRepo.save(registration);
            outboxService.writeToOutbox(registration, REGISTRATION_CREATE);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
