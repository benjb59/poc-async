package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationJMS;
import fr.insee.pocasync.producer.domain.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@RequiredArgsConstructor
@Component
public class RequestToConsumerJMS {

    private final JmsTemplate jmsTemplate;

    @Transactional
    public void publish(UserDTO user) {

        log.info("##################################");
        log.info("ACTIVEMQ - PRODUCER : send message with correlation_id: <" + user.getCorrelationId() + ">");
        log.info("##################################");

        jmsTemplate.convertAndSend(ConfigurationJMS.MESSAGE_QUEUE_REQUEST, "JMS received User : " + user.getUsername(), m -> {
            m.setJMSCorrelationID(user.getCorrelationId().toString());
            return m;
        });
    }
}
