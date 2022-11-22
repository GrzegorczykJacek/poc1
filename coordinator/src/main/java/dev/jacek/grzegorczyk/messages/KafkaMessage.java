package dev.jacek.grzegorczyk.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaMessage {

    Schema schema;
    Payload payload;
}




