package dev.jacek.grzegorczyk.registrator.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationDTO {

    @NotEmpty
    private String contents;
}
