package dev.jacek.grzegorczyk.repo;

import dev.jacek.grzegorczyk.entities.ApiMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiMessageRepo extends JpaRepository<ApiMessage, Long> {
}
