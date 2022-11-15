package dev.jacek.grzegorczyk.services;

import dev.jacek.grzegorczyk.entities.TransactionRegistration;
import dev.jacek.grzegorczyk.enumerations.TransactionState;
import dev.jacek.grzegorczyk.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryService {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final TransactionRepo transactionRepo;

    @Override
    public void recover(String transactionId) {
        log.info("Recovery call for transaction with id: {}", transactionId);
        try {
            Thread.sleep(3000);

            TransactionRegistration transactionRegistration = transactionRepo.findByTransactionId(transactionId).orElseThrow();
            transactionRegistration.setTransactionState(TransactionState.ROLLED_BACK);
            transactionRepo.save(transactionRegistration);
            restTemplate.delete("http://localhost:8081/api/v1/registrations/" + transactionId);

            rabbitTemplate.convertAndSend("notifier", "ROLLBACK transaction with it: " + transactionId);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
