package fr.insee.pocasync.consumer.broker.in;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix= "notification", name = "service", havingValue = "amqp")
public class ConsumeFromProducerAMQP {

    public void receiveMessage(String message) {
        log.info("##################################");
        log.info("RABBITMQ - CONSUMER : received message : " + message);
        log.info("##################################");
    }
}
