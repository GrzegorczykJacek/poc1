package dev.jacek.grzegorczyk.repo;

import dev.jacek.grzegorczyk.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxEventRepo extends JpaRepository<OutboxEvent, UUID> {
}
