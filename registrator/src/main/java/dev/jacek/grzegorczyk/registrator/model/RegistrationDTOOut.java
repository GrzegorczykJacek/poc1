package dev.jacek.grzegorczyk.registrator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationDTOOut extends RegistrationDTO {

    private Long id;
}
