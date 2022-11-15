package dev.jacek.grzegorczyk;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionRegistrationResponse extends TransactionRegistrationRequest {

    private Long id;

    private Boolean isTransactionOriginRolledBack;
}
