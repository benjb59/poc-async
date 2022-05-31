package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationAMQP;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Component
public class RequestToConsumerAMQP {

    private final RabbitTemplate rabbitTemplate;

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final UserRepository userRepository;

    @Value("${notification.service.mode}")
    private String mode;

    @Transactional
    public void publish(UserDTO userDTO) {

        log.info("##################################");
        log.info("RABBITMQ - PRODUCER : send message");
        log.info("##################################");

        if ("sync".equals(mode)) {
            rabbitTemplate.setReplyTimeout(60000);

            String response = (String) rabbitTemplate.convertSendAndReceive(
                    ConfigurationAMQP.EXCHANGE_NAME,
                    ConfigurationAMQP.ROUTING_KEY,
                    userDTO);

            if (response != null) {
                userDTO.setRegistered(true);
                userRepository.save(userDTO);
            }

        } else {
            asyncRabbitTemplate.setReceiveTimeout(60000);

            ListenableFuture<String> listenableFuture =
                    asyncRabbitTemplate.convertSendAndReceive(
                            ConfigurationAMQP.EXCHANGE_NAME,
                            ConfigurationAMQP.ROUTING_KEY,
                            userDTO);
            // non blocking part
            log.info("Non blocking block");

            try {
                String response = listenableFuture.get();
                log.info("Message received: {}", response);
                userDTO.setRegistered(true);
                userRepository.save(userDTO);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Cannot get response.", e);
            }
        }
        ;
    }
}
