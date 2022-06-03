package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.out.RequestToConsumerAMQP;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Service
public class UserServiceImplAMQP implements UserService {

    @Getter
    private final UserRepository userRepository;
    private final RequestToConsumerAMQP userProducer;

    @Override
    public void publishAndReceive(UserDTO userDTO) {
        userProducer.publish(userDTO);
    }

}
