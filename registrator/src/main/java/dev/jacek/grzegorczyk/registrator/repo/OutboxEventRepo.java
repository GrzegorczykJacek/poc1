package dev.jacek.grzegorczyk.registrator.repo;

import dev.jacek.grzegorczyk.registrator.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxEventRepo extends JpaRepository<OutboxEvent, UUID> {
}
