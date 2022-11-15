package dev.jacek.grzegorczyk.entities;

import dev.jacek.grzegorczyk.enumerations.TransactionState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TransactionRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String transactionOriginId;

    private String transactionId;

    private String serviceName;

    private TransactionState transactionState;
}
