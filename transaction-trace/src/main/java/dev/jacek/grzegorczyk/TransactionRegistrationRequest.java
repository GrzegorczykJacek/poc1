package dev.jacek.grzegorczyk;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TransactionRegistrationRequest {

    @NotEmpty
    private String transactionId;

    private String transactionOriginId;

    @NotEmpty
    private String serviceName;
}
