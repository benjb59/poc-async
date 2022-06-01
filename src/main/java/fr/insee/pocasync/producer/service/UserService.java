package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<String> createUser(String username);

    Flux<UserEntity> queryUser();
}
