package dev.jacek.grzegorczyk.registrator.entities;

import dev.jacek.grzegorczyk.registrator.enums.OutboxOperation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class OutboxEvent {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    private UUID id;

    @Column(name = "aggregateid")
    private String aggregateId;

    @Column(name = "aggregatetype")
    private String aggregateType;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OutboxOperation operation;

    private String payload;
}
