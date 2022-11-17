package dev.jacek.grzegorczyk.entities;

import dev.jacek.grzegorczyk.enums.OutboxOperation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class OutboxMessage {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    private UUID id;

    private String aggregate;

    @Enumerated(EnumType.STRING)
    private OutboxOperation operation;

    private String message;
}
