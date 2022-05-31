package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumerJMS;
import fr.insee.pocasync.producer.broker.out.RequestToConsumerJMS;
import fr.insee.pocasync.producer.domain.UserEntity;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;
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
    public String createUser(String username) {

        String correlationId = UUID.randomUUID().toString();
        userProducer.publish(username, correlationId);
        responseReceiverFromConsumer.receiveResponse(MESSAGE_QUEUE_RESPONSE, correlationId);

        /*UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        return userRepository.save(userEntity).getUserId().toString();*/
        return null;
    }

    @Override
    public Stream<UserEntity> queryUser() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false);
    }
}
