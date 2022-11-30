package dev.jacek.grzegorczyk.service;

import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;
import dev.jacek.grzegorczyk.repo.ApiMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.jacek.grzegorczyk.enums.OutboxOperation.API_MESSAGE_CREATE;
import static dev.jacek.grzegorczyk.enums.OutboxOperation.API_MESSAGE_UPDATE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {

    private final ApiMessageRepo apiMessageRepo;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public ApiMessageDTOOut create(ApiMessageDTO apiMessageDTO) {
        log.info("Save new ApiMessage: {}", apiMessageDTO);
        ApiMessage apiMessage = new ApiMessage();
        apiMessage.setMessage(apiMessageDTO.getMessage());
        apiMessage.setAuthor(apiMessageDTO.getAuthor());


        ApiMessage savedMessage = apiMessageRepo.save(apiMessage);

        ApiMessageDTOOut apiMessageDTOOut = new ApiMessageDTOOut();
        apiMessageDTOOut.setMessage(savedMessage.getMessage());
        apiMessageDTOOut.setAuthor(apiMessage.getAuthor());
        apiMessageDTOOut.setId(apiMessage.getId());

        outboxService.writeToOutbox(apiMessage, API_MESSAGE_CREATE);

        // This will fire an exception
//        generateRuntimeException();

        return apiMessageDTOOut;
    }

    @Override
    @Transactional
    public void setIsRegistered(Long id) {
        log.info("SET IS REGISTERED for apiMessage with id: {}",id);
        ApiMessage apiMessage = apiMessageRepo.findById(id).orElseThrow();
        apiMessage.setRegistered(true);
        apiMessageRepo.save(apiMessage);

        outboxService.writeToOutbox(apiMessage, API_MESSAGE_UPDATE);
    }

    private void generateRuntimeException() {
        throw new RuntimeException("OMG! Something went terribly wrong here!");
    }
}
