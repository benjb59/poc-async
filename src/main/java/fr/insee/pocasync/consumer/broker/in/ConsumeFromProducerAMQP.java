package fr.insee.pocasync.consumer.broker.in;

import fr.insee.pocasync.ConfigurationAMQP;
import fr.insee.pocasync.producer.domain.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Component
public class ConsumeFromProducerAMQP {

    @SneakyThrows
    @RabbitListener(queues = ConfigurationAMQP.MESSAGE_QUEUE_REQUEST)
    public String receiveMessage(UserDTO userDTO) {

        log.info("######################################");
        log.info("RABBITMQ - CONSUMER : received message");
        log.info("######################################");

        TimeUnit.SECONDS.sleep(10);

        return "Hello world, " + userDTO.getUsername();
    }
}
