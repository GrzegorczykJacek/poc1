package dev.jacek.grzegorczyk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.entities.OutboxEvent;
import dev.jacek.grzegorczyk.repo.OutboxEventRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.jacek.grzegorczyk.enums.OutboxOperation.API_MESSAGE_CREATE;
import static dev.jacek.grzegorczyk.service.OutboxService.API_MESSAGE_AGGREGATE;
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
        var apiMessage = new ApiMessage();
        apiMessage.setMessage("Test message...");
        apiMessage.setAuthor("Test author...");
        apiMessage.setId(1L);

        var message = "{\"id\": 1, \"message\": \"Test message...\"}";
        given(objectMapper.writeValueAsString(any())).willReturn(message);

        // When
        outboxService.writeToOutbox(apiMessage, API_MESSAGE_CREATE);

        // Then
        var outboxEventCaptor = ArgumentCaptor.forClass(OutboxEvent.class);
        var apiMessageCaptor = ArgumentCaptor.forClass(ApiMessage.class);
        verify(objectMapper, times(1)).writeValueAsString(apiMessageCaptor.capture());
        assertEquals(apiMessage.getId(), apiMessageCaptor.getValue().getId());
        assertEquals(apiMessage.getMessage(), apiMessageCaptor.getValue().getMessage());
        assertEquals(apiMessage.getAuthor(), apiMessageCaptor.getValue().getAuthor());

        verify(outboxEventRepo, times(1)).save(outboxEventCaptor.capture());
        assertEquals(message, outboxEventCaptor.getValue().getPayload());
        assertEquals(API_MESSAGE_CREATE, outboxEventCaptor.getValue().getOperation());
        assertEquals(apiMessage.getId().toString(), outboxEventCaptor.getValue().getAggregateId());
        assertEquals(API_MESSAGE_AGGREGATE, outboxEventCaptor.getValue().getAggregateType());
    }
}