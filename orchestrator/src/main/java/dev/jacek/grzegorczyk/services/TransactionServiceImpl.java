package dev.jacek.grzegorczyk.services;

import dev.jacek.grzegorczyk.TransactionRegistrationRequest;
import dev.jacek.grzegorczyk.TransactionRegistrationResponse;
import dev.jacek.grzegorczyk.entities.TransactionRegistration;
import dev.jacek.grzegorczyk.events.RollbackEvent;
import dev.jacek.grzegorczyk.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static dev.jacek.grzegorczyk.enumerations.TransactionState.COMMITTED;
import static dev.jacek.grzegorczyk.enumerations.TransactionState.ROLLED_BACK;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public TransactionRegistrationResponse create(TransactionRegistrationRequest registrationRequest) {
        log.info("Create new transaction: {}", registrationRequest);
        TransactionRegistration transactionRegistration = new TransactionRegistration();
        transactionRegistration.setTransactionId(registrationRequest.getTransactionId());
        String transactionOriginId = registrationRequest.getTransactionOriginId();
        if (transactionOriginId != null) {
            transactionRegistration.setTransactionOriginId(transactionOriginId);
        }
        transactionRegistration.setServiceName(registrationRequest.getServiceName());

        return toTransactionRegistrationResponse(transactionRegistration);
    }

    @Override
    public TransactionRegistrationResponse rollback(String transactionId) {
        log.info("Rollback transaction with id: {}", transactionId);
        TransactionRegistration transactionRegistration = transactionRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id: " + transactionId + " not found!"));
        transactionRegistration.setTransactionState(ROLLED_BACK);

        TransactionRegistrationResponse transactionRegistrationResponse = toTransactionRegistrationResponse(transactionRegistration);
        eventPublisher.publishEvent(new RollbackEvent(this, transactionId));
        return transactionRegistrationResponse;
    }

    @Override
    public TransactionRegistrationResponse commit(String transactionId) {
        log.info("Commit transaction with id: {}", transactionId);
        TransactionRegistration transactionRegistration = transactionRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id: " + transactionId + " not found!"));
        transactionRegistration.setTransactionState(COMMITTED);

        TransactionRegistrationResponse transactionRegistrationResponse = toTransactionRegistrationResponse(transactionRegistration);
        transactionRegistrationResponse.setIsTransactionOriginRolledBack(transactionRepo.isOriginRolledBack(transactionId));

        return transactionRegistrationResponse;
    }

    private TransactionRegistrationResponse toTransactionRegistrationResponse(TransactionRegistration transactionRegistration) {
        TransactionRegistration savedTransaction = transactionRepo.save(transactionRegistration);
        TransactionRegistrationResponse transactionRegistrationResponse = new TransactionRegistrationResponse();
        transactionRegistrationResponse.setServiceName(savedTransaction.getServiceName());
        transactionRegistrationResponse.setTransactionId(savedTransaction.getTransactionId());
        transactionRegistrationResponse.setTransactionOriginId(savedTransaction.getTransactionOriginId());
        return transactionRegistrationResponse;
    }
}
