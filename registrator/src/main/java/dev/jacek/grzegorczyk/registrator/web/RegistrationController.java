package dev.jacek.grzegorczyk.registrator.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jacek.grzegorczyk.TransactionEnvelope;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;
import dev.jacek.grzegorczyk.registrator.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public RegistrationDTOOut register(@RequestBody @Valid TransactionEnvelope envelope) throws JsonProcessingException {
        log.info("REGISTRATOR API register a new request for: {}", envelope);
        return registrationService.create(envelope);
    }

    @DeleteMapping("/{transactionId}")
    void delete(@PathVariable String transactionId) {
        log.info("REGISTRATOR API delete request for {}", transactionId);
        registrationService.deleteByTransactionId(transactionId);
    }
}
