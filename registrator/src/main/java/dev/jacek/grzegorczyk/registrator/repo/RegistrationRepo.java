package dev.jacek.grzegorczyk.registrator.repo;

import dev.jacek.grzegorczyk.registrator.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

    void deleteByTransactionId(String transactionId);
}
