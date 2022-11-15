package dev.jacek.grzegorczyk;

public interface TransactionService {

    String register(TransactionEnvelope envelope);

    void commit(String transactionId);
}
