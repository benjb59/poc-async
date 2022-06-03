package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationAMQP;
import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;
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
    public void publish(@NonNull UserDTO userDTO) {

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
                userRepository.setRegisteredForUserId(userDTO.userId(), true).subscribe();
            }

        }
        if ("future".equals(mode)) {
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
                userRepository.setRegisteredForUserId(userDTO.userId(), true).subscribe();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Cannot get response.", e);
            }
        } else {
            UUID correlationId = UUID.randomUUID();
            userRepository.setCorrelationIdForUserId(userDTO.userId(), correlationId).subscribe();

            MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setReplyTo(ConfigurationAMQP.MESSAGE_QUEUE_RESPONSE);
                messageProperties.setCorrelationId(correlationId.toString());
                return message;
            };

            rabbitTemplate.convertAndSend(
                    ConfigurationAMQP.EXCHANGE_NAME,
                    ConfigurationAMQP.ROUTING_KEY,
                    userDTO,
                    messagePostProcessor);
        }
    }
}
