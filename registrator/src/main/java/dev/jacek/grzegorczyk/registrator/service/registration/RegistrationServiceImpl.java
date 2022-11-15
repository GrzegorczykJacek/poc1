package dev.jacek.grzegorczyk.registrator.service.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.TransactionEnvelope;
import dev.jacek.grzegorczyk.TransactionService;
import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTO;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;
import dev.jacek.grzegorczyk.registrator.repo.RegistrationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final ObjectMapper objectMapper;
    private final RegistrationRepo registrationRepo;
    private final TransactionService transactionService;

    @Override
    @Transactional
    public RegistrationDTOOut create(TransactionEnvelope envelope) throws JsonProcessingException {

        String registeredTransactionId = transactionService.register(envelope);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                transactionService.commit(registeredTransactionId);
            }
        });

        RegistrationDTO registrationDTO = objectMapper.readValue(envelope.getPayload(), RegistrationDTO.class);
        log.info("REGISTRATOR API create new Registration for: {}", registrationDTO);
        Registration registration = new Registration();
        registration.setContents(registrationDTO.getContents());
        registration.setTransactionId(envelope.getTransactionOriginId());
        Registration savedRegistration = registrationRepo.save(registration);

        RegistrationDTOOut registrationDTOOut = new RegistrationDTOOut();
        registrationDTOOut.setContents(savedRegistration.getContents());
        registrationDTOOut.setId(savedRegistration.getId());

        return registrationDTOOut;
    }

    @Override
    @Transactional
    public void deleteByTransactionId(String transactionId) {
        registrationRepo.deleteByTransactionId(transactionId);
    }
}
