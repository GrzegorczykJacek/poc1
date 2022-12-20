package dev.jacek.grzegorczyk.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ApiMessageDTO {

    @NotEmpty
    private String message;

    @NotEmpty
    private String author;
}