package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumerJMS;
import fr.insee.pocasync.producer.broker.out.RequestToConsumerJMS;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;
import java.util.function.Function;

import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_RESPONSE;


@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@Service
public class UserServiceImplJMS implements UserService {

    private final UserRepository userRepository;
    private final RequestToConsumerJMS userProducer;
    private final ResponseFromConsumerJMS responseReceiverFromConsumer;

    @Override
    public Mono<String> createUser(String username) {
        return Mono.fromCallable(() ->{
            var userDTO = userRepository.save(new UserDTO(UUID.randomUUID(), username));
            userProducer.publish(userDTO);
            responseReceiverFromConsumer.receiveResponse(MESSAGE_QUEUE_RESPONSE, userDTO.getCorrelationId().toString());
            return (String)null;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<UserDTO> queryUser() {
        return Mono.fromCallable(userRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(Function.identity());
    }
}
