package dev.jacek.grzegorczyk.services;

import dev.jacek.grzegorczyk.TransactionRegistrationRequest;
import dev.jacek.grzegorczyk.TransactionRegistrationResponse;

public interface TransactionService {

    TransactionRegistrationResponse create(TransactionRegistrationRequest registrationRequest);

    TransactionRegistrationResponse rollback(String transactionId);

    TransactionRegistrationResponse commit(String transactionId);
}
