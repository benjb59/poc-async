package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

public interface UserService {
    default Mono<UserDTO> createUser(@NonNull String username) {
        final UserDTO[] wrapperUserDTO=new UserDTO[1];
        return getUserRepository().save(new UserDTO(null,UUID.randomUUID(), username,false))
                .doOnSuccess(u->wrapperUserDTO[0]=u)
                .log("reactor.", Level.INFO)
                .doAfterTerminate(()->publishAndReceive(wrapperUserDTO[0]));
    }

    void publishAndReceive(UserDTO userDTO);

    default Flux<UserDTO> queryUser() {
        return getUserRepository().findAll();
    }

    UserRepository getUserRepository();

}
