package dev.jacek.grzegorczyk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.TransactionEnvelope;
import dev.jacek.grzegorczyk.TransactionRegistrationRequest;
import dev.jacek.grzegorczyk.TransactionRegistrationResponse;
import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;
import dev.jacek.grzegorczyk.model.RegistrationRequest;
import dev.jacek.grzegorczyk.model.RegistrationResponse;
import dev.jacek.grzegorczyk.repo.ApiMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {

    private final ApiMessageRepo apiMessageRepo;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public ApiMessageDTOOut create(ApiMessageDTO apiMessageDTO) {
        final String transactionId = UUID.randomUUID().toString();
        applicationEventPublisher.publishEvent(transactionId);

        registerTransaction(transactionId);

        registerApiMessage(transactionId, apiMessageDTO);
        sendToRabbitMQ(apiMessageDTO);

        log.info("Save new ApiMessage: {}", apiMessageDTO);
        ApiMessage apiMessage = new ApiMessage();
        apiMessage.setMessage(apiMessageDTO.getMessage());
        apiMessage.setAuthor(apiMessageDTO.getAuthor());

        generateRuntimeException();

        ApiMessage savedMessage = apiMessageRepo.save(apiMessage);

        ApiMessageDTOOut apiMessageDTOOut = new ApiMessageDTOOut();
        apiMessageDTOOut.setMessage(savedMessage.getMessage());
        apiMessageDTOOut.setAuthor(apiMessage.getAuthor());
        apiMessageDTOOut.setId(apiMessage.getId());

        return apiMessageDTOOut;
    }

    private void sendToRabbitMQ(ApiMessageDTO apiMessageDTO) {
        log.info("ADD TO QUEUE! {}", apiMessageDTO);
        rabbitTemplate.convertAndSend("notifier", apiMessageDTO.toString());
    }

    private void registerApiMessage(final String transactionOriginId, ApiMessageDTO apiMessageDTO) {
        log.info("NOTIFY REGISTRATOR! {}", apiMessageDTO);
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setContents(apiMessageDTO.toString());

        TransactionEnvelope transactionEnvelope = new TransactionEnvelope();
        try {
            String payload = objectMapper.writeValueAsString(registrationRequest);
            transactionEnvelope.setPayload(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        transactionEnvelope.setTransactionOriginId(transactionOriginId);

        ResponseEntity<RegistrationResponse> response = restTemplate.postForEntity("http://localhost:8081/api/v1/registrations",
                transactionEnvelope,
                RegistrationResponse.class);
        log.info("REGISTRATOR API registered request: {}", response.getBody());
    }

    private void registerTransaction(final String transactionId) {
        log.info("REGISTER TRANSACTION! {}", transactionId);
        TransactionRegistrationRequest transactionRegistrationRequest = new TransactionRegistrationRequest();
        transactionRegistrationRequest.setTransactionId(transactionId);
        transactionRegistrationRequest.setServiceName("coordinator");

        ResponseEntity<TransactionRegistrationResponse> response = restTemplate.postForEntity("http://localhost:8083/api/v1/transactions",
                transactionRegistrationRequest,
                TransactionRegistrationResponse.class);
        log.info("TRANSACTION REGISTERED: {}", response.getBody());
    }

    private void generateRuntimeException() {
        throw new RuntimeException("OMG! Something went terribly wrong here!");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollBackOrder(String transactionId) {
        log.info("ROLLBACK REGISTERED TRANSACTION: {}", transactionId);
        restTemplate.postForEntity("http://localhost:8083/api/v1/transactions/" + transactionId + "/rollback",
                transactionId,
                TransactionRegistrationResponse.class);
    }
}
