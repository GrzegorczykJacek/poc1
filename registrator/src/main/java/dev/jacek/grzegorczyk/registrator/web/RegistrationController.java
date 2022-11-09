package dev.jacek.grzegorczyk.registrator.web;

import dev.jacek.grzegorczyk.registrator.model.RegistrationDTO;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;
import dev.jacek.grzegorczyk.registrator.service.RegistrationService;
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
    public RegistrationDTOOut register(@RequestBody @Valid RegistrationDTO registrationDTO) {
        log.info("REGISTRATOR API register a new request for: {}", registrationDTO);
        return registrationService.create(registrationDTO);
    }
}
