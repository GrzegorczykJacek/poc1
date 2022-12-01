package dev.jacek.grzegorczyk.registrator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.registrator.entities.OutboxEvent;
import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.repo.OutboxEventRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static dev.jacek.grzegorczyk.registrator.enums.OutboxOperation.REGISTRATION_CREATE;
import static dev.jacek.grzegorczyk.registrator.services.OutboxService.REGISTRATION_AGGREGATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutboxServiceImplTest {

    @Mock
    private OutboxEventRepo outboxEventRepo;
    @Mock
    private ObjectMapper objectMapper;

    private OutboxService outboxService;

    @BeforeEach
    public void setUp() {
        outboxService = new OutboxServiceImpl(
                outboxEventRepo,
                objectMapper);
    }

    @Test
    void shouldWriteToOutbox() throws JsonProcessingException {
        // Given
        var uuid = UUID.randomUUID().toString();
        var message = "{\"id\": 1, \"messageId\": 1, \"uuid\": \"" + uuid + "\", \"contents\": \"Some contents...\"}";

        var registration = new Registration();
        registration.setId(1L);
        registration.setUuid(UUID.randomUUID().toString());
        registration.setMessageId(1L);
        registration.setContents(message);

        given(objectMapper.writeValueAsString(any())).willReturn(message);

        // When
        outboxService.writeToOutbox(registration, REGISTRATION_CREATE);

        // Then
        var outboxEventCaptor = ArgumentCaptor.forClass(OutboxEvent.class);
        var registrationCaptor = ArgumentCaptor.forClass(Registration.class);
        verify(objectMapper, times(1)).writeValueAsString(registrationCaptor.capture());
        assertEquals(registration.getId(), registrationCaptor.getValue().getId());
        assertEquals(registration.getMessageId(), registrationCaptor.getValue().getMessageId());
        assertEquals(registration.getUuid(), registrationCaptor.getValue().getUuid());
        assertEquals(registration.getContents(), registrationCaptor.getValue().getContents());

        verify(outboxEventRepo, times(1)).save(outboxEventCaptor.capture());
        assertEquals(message, outboxEventCaptor.getValue().getPayload());
        assertEquals(REGISTRATION_CREATE, outboxEventCaptor.getValue().getOperation());
        assertEquals(registration.getId().toString(), outboxEventCaptor.getValue().getAggregateId());
        assertEquals(REGISTRATION_AGGREGATE, outboxEventCaptor.getValue().getAggregateType());
    }
}