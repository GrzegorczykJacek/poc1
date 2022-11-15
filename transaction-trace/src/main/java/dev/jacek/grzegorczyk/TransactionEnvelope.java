package dev.jacek.grzegorczyk;

import lombok.Data;

@Data
public class TransactionEnvelope {

    private String transactionOriginId;

    private String payload;
}
