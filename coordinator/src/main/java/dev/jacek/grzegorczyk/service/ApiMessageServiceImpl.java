package dev.jacek.grzegorczyk.service;

import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;
import dev.jacek.grzegorczyk.model.RegistrationRequest;
import dev.jacek.grzegorczyk.model.RegistrationResponse;
import dev.jacek.grzegorczyk.repo.ApiMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {

    private final ApiMessageRepo apiMessageRepo;

    private final RestTemplate restTemplate;

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public ApiMessageDTOOut create(ApiMessageDTO apiMessageDTO) {
        registerApiMessage(apiMessageDTO);
        sendToRabbitMQ(apiMessageDTO);

        log.info("Save new ApiMessage: {}", apiMessageDTO);
        ApiMessage apiMessage = new ApiMessage();
        apiMessage.setMessage(apiMessageDTO.getMessage());
        apiMessage.setAuthor(apiMessageDTO.getAuthor());

        // This will fire an exception
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

    private void registerApiMessage(ApiMessageDTO apiMessageDTO) {
        log.info("NOTIFY REGISTRATOR! {}", apiMessageDTO);
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setContents(apiMessageDTO.toString());
        ResponseEntity<RegistrationResponse> response = restTemplate.postForEntity("http://localhost:8081/api/v1/registrations",
                registrationRequest,
                RegistrationResponse.class);
        log.info("REGISTRATOR API registered request: {}", response);
    }

    private void generateRuntimeException() {
        throw new RuntimeException("OMG! Something went terribly wrong here!");
    }
}
