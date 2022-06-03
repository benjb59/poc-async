package fr.insee.pocasync.producer.repository;

import fr.insee.pocasync.producer.domain.UserDTO;
import lombok.NonNull;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Flow;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserDTO, UUID> {

    Mono<UserDTO> findByUsername(String username);
    Mono<UserDTO> findByCorrelationId(UUID correlationId);

    @Modifying
    @Query("update user_dto set registered = :registered where user_id = :userId")
    Mono<Integer> setRegisteredForUserId(@NonNull Long userId, boolean registered);

    @Modifying
    @Query("update user_dto set correlation_id = :correlationId where user_id = :userId")
    Mono<Integer> setCorrelationIdForUserId(Long userId, UUID correlationId);
}
