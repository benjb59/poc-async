package fr.insee.pocasync.producer.broker.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static fr.insee.Configurations.MESSAGE_QUEUE_REQUEST;

@Slf4j
@Component
public class RequestToConsumer {

    @Autowired
    JmsTemplate jmsTemplate;
    public void publish(String username, String correlationId) {

        jmsTemplate.convertAndSend(MESSAGE_QUEUE_REQUEST, "create user "+username ,  m -> {
            m.setJMSCorrelationID(correlationId);
            return m;
        });

    }
}
