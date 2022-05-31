package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumerJMS;
import fr.insee.pocasync.producer.broker.out.RequestToConsumerAMQP;
import fr.insee.pocasync.producer.domain.UserEntity;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
public class UserServiceImplAMQP implements UserService {

    private final UserRepository userRepository;
    private final RequestToConsumerAMQP userProducer;
    private final ResponseFromConsumerJMS responseReceiverFromConsumer;

    @Override
    public String createUser(String username) {
        userProducer.publish(username);
        return null;
    }

    @Override
    public Stream<UserEntity> queryUser() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false);
    }
}
