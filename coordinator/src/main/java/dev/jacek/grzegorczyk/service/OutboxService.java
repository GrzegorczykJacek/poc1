package dev.jacek.grzegorczyk.service;

import dev.jacek.grzegorczyk.entities.ApiMessage;
import dev.jacek.grzegorczyk.enums.OutboxOperation;

public interface OutboxService {

    String API_MESSAGE_AGGREGATE = "ApiMessage";

    void writeToOutbox(ApiMessage apiMessage, OutboxOperation operation);
}
