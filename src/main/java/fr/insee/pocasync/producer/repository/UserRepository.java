package fr.insee.pocasync.producer.repository;

import fr.insee.pocasync.producer.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    UserEntity findByUsername(String username);
}
