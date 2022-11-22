package dev.jacek.grzegorczyk.registrator.services;

import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.enums.OutboxOperation;

public interface OutboxService {

    String REGISTRATION_AGGREGATE = "Registration";

    void writeToOutbox(Registration registration, OutboxOperation operation);
}
