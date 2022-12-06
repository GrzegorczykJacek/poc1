package dev.jacek.grzegorczyk.registrator.messages;

import lombok.Data;

@Data
public class ApiMessagePayload {

    private Long id;

    private String author;

    private String message;

    private boolean registered;
}
