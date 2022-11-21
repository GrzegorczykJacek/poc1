package dev.jacek.grzegorczyk.registrator.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payload {

    private String before = null;

    After after;

    Source source;

    private String op;

    private float ts_ms;

    private String transaction = null;
}