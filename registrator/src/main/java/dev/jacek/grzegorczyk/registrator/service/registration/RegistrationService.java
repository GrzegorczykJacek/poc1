package dev.jacek.grzegorczyk.registrator.service.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jacek.grzegorczyk.TransactionEnvelope;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;

public interface RegistrationService {

    RegistrationDTOOut create(TransactionEnvelope envelope) throws JsonProcessingException;

    void deleteByTransactionId(String transactionId);
}
