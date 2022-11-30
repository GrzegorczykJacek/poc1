package dev.jacek.grzegorczyk.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationPayload {

    private Long id;

    private String uuid;

    private String contents;
}
