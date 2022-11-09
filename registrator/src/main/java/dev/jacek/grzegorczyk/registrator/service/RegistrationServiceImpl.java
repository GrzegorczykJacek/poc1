package dev.jacek.grzegorczyk.registrator.service;

import dev.jacek.grzegorczyk.registrator.entities.Registration;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTO;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;
import dev.jacek.grzegorczyk.registrator.repo.RegistrationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepo registrationRepo;

    @Override
    public RegistrationDTOOut create(RegistrationDTO registrationDTO) {

        log.info("REGISTRATOR API create new Registration for: {}", registrationDTO);
        Registration registration = new Registration();
        registration.setContents(registrationDTO.getContents());
        Registration savedRegistration = registrationRepo.save(registration);

        RegistrationDTOOut registrationDTOOut = new RegistrationDTOOut();
        registrationDTOOut.setContents(savedRegistration.getContents());
        registrationDTOOut.setId(savedRegistration.getId());

        return registrationDTOOut;
    }
}
