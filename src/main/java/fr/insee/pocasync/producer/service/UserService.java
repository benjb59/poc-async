package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserDTO;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<String> createUser(@NonNull String username);

    Flux<UserDTO> queryUser();

}
