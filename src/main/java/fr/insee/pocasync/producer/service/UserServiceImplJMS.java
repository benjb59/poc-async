package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumerJMS;
import fr.insee.pocasync.producer.broker.out.RequestToConsumerJMS;
import fr.insee.pocasync.producer.domain.UserEntity;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_RESPONSE;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
public class UserServiceImplJMS implements UserService {

    private final UserRepository userRepository;
    private final RequestToConsumerJMS userProducer;
    private final ResponseFromConsumerJMS responseReceiverFromConsumer;

    @Override
    public Mono<String> createUser(String username) {
        return Mono.fromCallable(() ->{
            String correlationId =UUID.randomUUID().toString();
            userProducer.publish(username, correlationId);
            responseReceiverFromConsumer.receiveResponse(MESSAGE_QUEUE_RESPONSE, correlationId);
            return (String)null;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<UserEntity> queryUser() {
        return Mono.fromCallable(userRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(Function.identity());
    }
}
