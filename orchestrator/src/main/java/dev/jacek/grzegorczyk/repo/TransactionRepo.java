package dev.jacek.grzegorczyk.repo;

import dev.jacek.grzegorczyk.entities.TransactionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepo extends JpaRepository<TransactionRegistration, Long> {
    Optional<TransactionRegistration> findByTransactionId(String transactionId);

    @Query("SELECT tr.transactionState FROM TransactionRegistration tr WHERE tr.transactionOriginId IS NULL AND tr.transactionId = :transactionId")
    Boolean isOriginRolledBack(@Param("transactionId") String transactionId);
}
