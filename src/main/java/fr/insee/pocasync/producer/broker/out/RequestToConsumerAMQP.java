package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationAMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
@Component
public class RequestToConsumerAMQP {

    private final RabbitTemplate rabbitTemplate;

    public RequestToConsumerAMQP(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String username) {
        log.info("##################################");
        log.info("RABBITMQ - PRODUCER : send message");
        log.info("##################################");

        rabbitTemplate.convertAndSend(ConfigurationAMQP.MESSAGE_QUEUE_REQUEST, "foo.bar.baz", "Hello from RabbitMQ!");
    }

}
