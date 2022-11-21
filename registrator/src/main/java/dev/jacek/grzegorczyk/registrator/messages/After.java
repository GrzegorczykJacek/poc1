package dev.jacek.grzegorczyk.registrator.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class After {

    private long id;
    private String author;
    private String message;
}
