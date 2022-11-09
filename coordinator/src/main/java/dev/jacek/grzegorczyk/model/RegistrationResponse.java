package dev.jacek.grzegorczyk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationResponse extends RegistrationRequest {

    private Long id;
}
