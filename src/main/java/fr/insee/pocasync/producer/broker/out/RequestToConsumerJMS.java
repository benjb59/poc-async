package fr.insee.pocasync.producer.broker.out;

import fr.insee.pocasync.ConfigurationJMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
public class RequestToConsumerJMS {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public void publish(String username, String jmsCorrelationId) {

        log.info("##################################");
        log.info("ACTIVEMQ - PRODUCER : send message with correlation_id: <" + jmsCorrelationId + ">");
        log.info("##################################");

        jmsTemplate.convertAndSend(ConfigurationJMS.MESSAGE_QUEUE_REQUEST, "JMS received User : " + username, m -> {
            m.setJMSCorrelationID(jmsCorrelationId);
            return m;
        });
    }
}
