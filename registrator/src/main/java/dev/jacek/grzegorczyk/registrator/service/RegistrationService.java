package dev.jacek.grzegorczyk.registrator.service;

import dev.jacek.grzegorczyk.registrator.model.RegistrationDTO;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;

public interface RegistrationService {

    RegistrationDTOOut create(RegistrationDTO registrationDTO);
}
