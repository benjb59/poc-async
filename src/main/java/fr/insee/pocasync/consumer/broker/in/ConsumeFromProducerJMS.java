package fr.insee.pocasync.consumer.broker.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_REQUEST;
import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_RESPONSE;
import static org.springframework.jms.support.JmsHeaders.CORRELATION_ID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@Component
public class ConsumeFromProducerJMS {

    private final JmsTemplate consumerJmsTemplate;

    @Transactional
    @JmsListener(destination = MESSAGE_QUEUE_REQUEST)
    public void receiveMessage(Message message) {

        log.info("##################################");
        log.info("ACTIVEMQ - CONSUMER : received message with correlation_id: <" + message.getHeaders().get(CORRELATION_ID) + ">");
        log.info("##################################");

        String correlationId = (String) message.getHeaders().get(CORRELATION_ID);

        consumerJmsTemplate.convertAndSend(MESSAGE_QUEUE_RESPONSE, "received Ok from Consumer", m -> {
            m.setJMSCorrelationID(correlationId);
            return m;
        });
    }
}
