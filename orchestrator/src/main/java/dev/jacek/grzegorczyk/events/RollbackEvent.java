package dev.jacek.grzegorczyk.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RollbackEvent extends ApplicationEvent {

    private final String transactionId;

    public RollbackEvent(Object source, String transactionId) {
        super(source);
        this.transactionId = transactionId;
    }
}
