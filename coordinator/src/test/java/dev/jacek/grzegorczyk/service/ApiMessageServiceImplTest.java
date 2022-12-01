package dev.jacek.grzegorczyk.service;

import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.enums.OutboxOperation;
import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.repo.ApiMessageRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApiMessageServiceImplTest {

    @Mock
    private OutboxService outboxService;
    @Mock
    private ApiMessageRepo apiMessageRepo;
    private ApiMessageService apiMessageService;

    @BeforeEach
    public void setUp() {
        apiMessageService = new ApiMessageServiceImpl(
                apiMessageRepo,
                outboxService
        );
    }

    @Test
    void shouldCreateNewApiMessage() {
        // Given
        ApiMessageDTO apiMessageDTO = new ApiMessageDTO();
        apiMessageDTO.setMessage("Test message...");
        apiMessageDTO.setAuthor("Test author...");

        ApiMessage apiMessage = new ApiMessage();
        apiMessage.setMessage(apiMessageDTO.getMessage());
        apiMessage.setAuthor(apiMessageDTO.getAuthor());

        ApiMessage savedApiMessage = new ApiMessage();
        savedApiMessage.setMessage(apiMessageDTO.getMessage());
        savedApiMessage.setAuthor(apiMessageDTO.getAuthor());

        given(apiMessageRepo.save(any())).willReturn(savedApiMessage);

        // When
        apiMessageService.create(apiMessageDTO);

        // Then
        ArgumentCaptor<ApiMessage> apiMessageCaptor = ArgumentCaptor.forClass(ApiMessage.class);
        ArgumentCaptor<OutboxOperation> outboxOperationCaptor = ArgumentCaptor.forClass(OutboxOperation.class);
        verify(apiMessageRepo, times(1)).save(apiMessageCaptor.capture());
        verify(outboxService, times(1)).writeToOutbox(apiMessageCaptor.capture(), outboxOperationCaptor.capture());
    }

    @Test
    void shouldSetIsRegisteredById() {
        // Given
        Long id = 1L;

        ApiMessage savedApiMessage = new ApiMessage();
        savedApiMessage.setMessage("Test message...");
        savedApiMessage.setAuthor("Test author...");

        given(apiMessageRepo.findById(any())).willReturn(Optional.of(savedApiMessage));

        // When
        apiMessageService.setIsRegistered(id);

        // Then
        ArgumentCaptor<ApiMessage> apiMessageCaptor = ArgumentCaptor.forClass(ApiMessage.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<OutboxOperation> outboxOperationCaptor = ArgumentCaptor.forClass(OutboxOperation.class);
        verify(apiMessageRepo, times(1)).findById(idCaptor.capture());
        verify(apiMessageRepo, times(1)).save(apiMessageCaptor.capture());
        verify(outboxService, times(1)).writeToOutbox(apiMessageCaptor.capture(), outboxOperationCaptor.capture());
    }
}