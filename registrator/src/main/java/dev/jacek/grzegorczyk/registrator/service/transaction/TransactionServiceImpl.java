package dev.jacek.grzegorczyk.registrator.service.transaction;

import dev.jacek.grzegorczyk.TransactionEnvelope;
import dev.jacek.grzegorczyk.TransactionRegistrationRequest;
import dev.jacek.grzegorczyk.TransactionRegistrationResponse;
import dev.jacek.grzegorczyk.TransactionService;
import dev.jacek.grzegorczyk.registrator.repo.RegistrationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final RestTemplate restTemplate;
    private final RegistrationRepo registrationRepo;

    @Override
    public String register(TransactionEnvelope envelope) {
        String transactionId = UUID.randomUUID().toString();
        log.info("REGISTER TRANSACTION! {}", transactionId);
        TransactionRegistrationRequest transactionRegistrationRequest = new TransactionRegistrationRequest();
        transactionRegistrationRequest.setTransactionId(transactionId);
        transactionRegistrationRequest.setServiceName("registrator");
        transactionRegistrationRequest.setTransactionOriginId(envelope.getTransactionOriginId());

        ResponseEntity<TransactionRegistrationResponse> response = restTemplate.postForEntity("http://localhost:8083/api/v1/transactions",
                transactionRegistrationRequest,
                TransactionRegistrationResponse.class);
        log.info("TRANSACTION REGISTERED: {}", response.getBody());
        return transactionId;
    }

    @Override
    public void commit(String transactionId) {
        ResponseEntity<TransactionRegistrationResponse> response = restTemplate.postForEntity("http://localhost:8083/api/v1/transactions/" + transactionId + "/commit",
                transactionId,
                TransactionRegistrationResponse.class);
        TransactionRegistrationResponse body = response.getBody();
        log.info("TRANSACTION REGISTERED: {}", body);

        Boolean isTransactionOriginRolledBack = body.getIsTransactionOriginRolledBack();
        if (isTransactionOriginRolledBack != null && isTransactionOriginRolledBack) {
            registrationRepo.deleteByTransactionId(transactionId);
        }
    }
}
