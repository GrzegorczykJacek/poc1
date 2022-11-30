package dev.jacek.grzegorczyk.registrator.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiMessagePayload {

    private Long id;

    private String author;

    private String message;

    private boolean registered;
}
