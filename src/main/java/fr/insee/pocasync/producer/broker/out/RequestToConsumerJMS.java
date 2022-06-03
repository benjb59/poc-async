package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationJMS;
import fr.insee.pocasync.producer.domain.UserDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@RequiredArgsConstructor
@Component
public class RequestToConsumerJMS {

    private final JmsTemplate jmsTemplate;

    public void publish(@NonNull UserDTO userDTO) {
        log.info("##################################");
        log.info("ACTIVEMQ - PRODUCER : send message with correlation_id: <" + userDTO.correlationId() + ">");
        log.info("##################################");

        jmsTemplate.convertAndSend(ConfigurationJMS.MESSAGE_QUEUE_REQUEST, "JMS received User : " + userDTO.username(), m -> {
            m.setJMSCorrelationID(userDTO.correlationId().toString());
            return m;
        });
    }
}
