package dev.jacek.grzegorczyk.events;

import dev.jacek.grzegorczyk.services.RecoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RollbackEventListener {

    private final RecoveryService recoveryService;

    @EventListener
    public void handleContextStart(RollbackEvent rollbackEvent) {
        log.info("Handling rollback event: {}", rollbackEvent);
        recoveryService.recover(rollbackEvent.getTransactionId());
    }
}
