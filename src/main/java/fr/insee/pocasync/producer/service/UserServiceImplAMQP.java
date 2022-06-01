package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.out.RequestToConsumerAMQP;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Service
public class UserServiceImplAMQP implements UserService {

    private final UserRepository userRepository;
    private final RequestToConsumerAMQP userProducer;

    @Override
    public Mono<String> createUser(String username) {

/*        UserDTO userDTO = UserDTO
                .builder()
                .username(username)
                .build();

        userRepository.save(userDTO);

        userProducer.publish(userDTO);
        return null;*/
        return Mono.empty();
    }

    @Override
    public Flux<UserDTO> queryUser() {
        //return StreamSupport.stream(userRepository.findAll().spliterator(), false);
        return Flux.empty();
    }
}
