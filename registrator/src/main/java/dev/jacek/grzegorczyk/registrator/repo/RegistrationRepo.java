package dev.jacek.grzegorczyk.registrator.repo;

import dev.jacek.grzegorczyk.registrator.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {
    Optional<Registration> findByMessageId(Long messageId);
}
