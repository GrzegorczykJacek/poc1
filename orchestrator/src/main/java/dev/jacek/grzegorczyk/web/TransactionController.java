package dev.jacek.grzegorczyk.web;

import dev.jacek.grzegorczyk.TransactionRegistrationRequest;
import dev.jacek.grzegorczyk.TransactionRegistrationResponse;
import dev.jacek.grzegorczyk.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(CREATED)
    public TransactionRegistrationResponse create(@RequestBody @Valid TransactionRegistrationRequest registrationRequest) {
        return transactionService.create(registrationRequest);
    }

    @PostMapping("/{transactionId}/rollback")
    @ResponseStatus(CREATED)
    public TransactionRegistrationResponse rollback(@PathVariable String transactionId) {
        return transactionService.rollback(transactionId);
    }

    @PostMapping("/{transactionId}/commit")
    @ResponseStatus(CREATED)
    public TransactionRegistrationResponse commit(@PathVariable String transactionId) {
        return transactionService.commit(transactionId);
    }
}
