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

import java.util.Optional;

import static dev.jacek.grzegorczyk.registrator.enums.OutboxOperation.REGISTRATION_CREATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final ObjectMapper objectMapper;
    private final RegistrationRepo registrationRepo;
    private final OutboxService outboxService;

    @KafkaListener(topics = "outbox.event.ApiMessage",
            groupId = "registrator_group_id")
    void listener(String data) {

        try {
            KafkaMessage kafkaMessage = objectMapper.readValue(data, new TypeReference<>() {
            });
            Payload payload = kafkaMessage.getPayload();
            String uuid = payload.getUuid();

            String messagePayload = payload.getPayload();
            ApiMessagePayload apiMessagePayload = objectMapper.readValue(messagePayload, new TypeReference<>() {
            });
            Long id = apiMessagePayload.getId();
            String author = apiMessagePayload.getAuthor();
            String message = apiMessagePayload.getMessage();
            boolean isRegistered = apiMessagePayload.isRegistered();
            log.info("KAFKA MESSAGE DESERIALIZED:uuid: {}, id: {}, author: {}, message: {}", uuid, id, author, message);

            Optional<Registration> optionalRegistrationToUpdate = registrationRepo.findByMessageId(id);
            if (optionalRegistrationToUpdate.isEmpty()) {
                log.info("CREATE REGISTRATION: {}", apiMessagePayload);
                Registration registration = new Registration();
                registration.setContents("{\"id\": " + id + ", \"author\": \"" + author + "\", \"message\": \"" + message + "\", \"registered\": \"" + isRegistered + "\"}");
                registration.setUuid(uuid);
                registration.setMessageId(apiMessagePayload.getId());
                registrationRepo.save(registration);
                outboxService.writeToOutbox(registration, REGISTRATION_CREATE);
            } else {
                log.info("UPDATE REGISTRATION with uuid: {} and contents: {}", uuid, apiMessagePayload);
                Registration registrationToUpdate = optionalRegistrationToUpdate.get();
                registrationToUpdate.setContents(messagePayload);
                registrationRepo.save(registrationToUpdate);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
