package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

public interface UserService {
    default Mono<String> createUser(@NonNull String username) {
        return Mono.fromCallable(() ->{
            var userDTO = getUserRepository().save(new UserDTO(null,UUID.randomUUID(), username,false)).block();
            publishAndReceive(userDTO);
            return (String)null;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    void publishAndReceive(UserDTO userDTO);

    default Flux<UserDTO> queryUser() {
        return getUserRepository().findAll();
    }

    UserRepository getUserRepository();

}
