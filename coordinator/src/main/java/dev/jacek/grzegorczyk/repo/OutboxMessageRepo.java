package dev.jacek.grzegorczyk.repo;

import dev.jacek.grzegorczyk.entities.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxMessageRepo extends JpaRepository<OutboxMessage, UUID> {
}
