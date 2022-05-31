package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumerJMS;
import fr.insee.pocasync.producer.broker.out.RequestToConsumerJMS;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public void createUser(String username) {

        UUID correlationId = UUID.randomUUID();

        UserDTO user = UserDTO
                .builder()
                .username(username)
                .correlationId(correlationId)
                .build();

        userRepository.save(user);

        userProducer.publish(user);

        responseReceiverFromConsumer.receiveResponse(MESSAGE_QUEUE_RESPONSE, correlationId.toString());
    }

    @Override
    public Stream<UserDTO> queryUser() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false);
    }
}
