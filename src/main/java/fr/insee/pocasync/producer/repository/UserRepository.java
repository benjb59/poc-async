package fr.insee.pocasync.producer.repository;

import fr.insee.pocasync.producer.domain.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserDTO, UUID> {

    UserDTO findByUsername(String username);

    UserDTO findByCorrelationId(UUID correlationId);
}
