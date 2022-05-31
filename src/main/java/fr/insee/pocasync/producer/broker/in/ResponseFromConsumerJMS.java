package fr.insee.pocasync.producer.broker.in;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
public class ResponseFromConsumerJMS {

    @Autowired
    JmsTemplate jmsTemplate;

    @Transactional
    public void receiveResponse(String destination, String jmsCorrelationId) {
        String response = (String) jmsTemplate.receiveSelectedAndConvert(destination, "JMSCorrelationID = '" + jmsCorrelationId + "'");
        log.info("##################################");
        log.info("ACTIVEMQ - PRODUCER : Ok from consumer for correlation_id <" + jmsCorrelationId + ">");
        log.info("##################################");
    }
}
