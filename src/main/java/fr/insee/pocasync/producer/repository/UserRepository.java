package fr.insee.pocasync.producer.repository;

import fr.insee.pocasync.producer.domain.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserDTO, UUID> {

    Mono<UserDTO> findByUsername(String username);
    Mono<UserDTO> findByCorrelationId(UUID correlationId);

}
