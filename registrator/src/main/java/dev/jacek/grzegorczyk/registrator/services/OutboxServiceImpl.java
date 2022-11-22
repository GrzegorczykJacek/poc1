package dev.jacek.grzegorczyk.registrator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.registrator.entities.OutboxEvent;
import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.enums.OutboxOperation;
import dev.jacek.grzegorczyk.registrator.repo.OutboxEventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxEventRepo outboxEventRepo;
    private final ObjectMapper objectMapper;

    @Override
    public void writeToOutbox(Registration registration, OutboxOperation operation) {
        log.info("OUTBOX: {} with: {}", operation, registration);
        OutboxEvent outboxEvent = new OutboxEvent();

        try {
            String message = objectMapper.writeValueAsString(registration);
            outboxEvent.setPayload(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxEvent.setOperation(operation);
        outboxEvent.setAggregateType(REGISTRATION_AGGREGATE);
        outboxEvent.setAggregateId(registration.getId().toString());

        outboxEventRepo.save(outboxEvent);
    }
}
