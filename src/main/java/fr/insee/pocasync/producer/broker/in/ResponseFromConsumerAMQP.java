package fr.insee.pocasync.producer.broker.in;

import fr.insee.pocasync.ConfigurationAMQP;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Component
public class ResponseFromConsumerAMQP {

    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional
    @RabbitListener(queues = ConfigurationAMQP.MESSAGE_QUEUE_RESPONSE, concurrency = "100")
    public void receiveMessage(Message message) {

        String correlationId = message.getMessageProperties().getCorrelationId();

        log.info("##################################");
        log.info("ACTIVEMQ - PRODUCER : Ok from consumer for correlation_id <" + correlationId + ">");
        log.info("##################################");

        userRepository.findByCorrelationId(UUID.fromString(correlationId)).subscribe(
                userDTO -> {
                    userDTO.withRegistered(true);
                    userRepository.save(userDTO);
                }
        );
    }
}
