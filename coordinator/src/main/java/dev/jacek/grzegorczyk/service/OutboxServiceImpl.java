package dev.jacek.grzegorczyk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.entities.OutboxEvent;
import dev.jacek.grzegorczyk.enums.OutboxOperation;
import dev.jacek.grzegorczyk.repo.OutboxMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxMessageRepo outboxMessageRepo;
    private final ObjectMapper objectMapper;

    @Override
    public void writeToOutbox(ApiMessage apiMessage, OutboxOperation operation) {
        log.info("OUTBOX: {} with: {}", operation, apiMessage);
        OutboxEvent outboxEvent = new OutboxEvent();

        try {
            String message = objectMapper.writeValueAsString(apiMessage);
            outboxEvent.setPayload(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxEvent.setOperation(operation);
        outboxEvent.setAggregateType(API_MESSAGE_AGGREGATE);
        outboxEvent.setAggregateId(apiMessage.getId().toString());

        outboxMessageRepo.save(outboxEvent);
    }
}
