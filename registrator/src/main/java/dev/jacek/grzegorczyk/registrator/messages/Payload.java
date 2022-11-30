package dev.jacek.grzegorczyk.registrator.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payload {

    private String payload;
    private String uuid;
}