package fr.insee.pocasync.consumer.broker.in;

import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_REQUEST;
import static fr.insee.pocasync.ConfigurationJMS.MESSAGE_QUEUE_RESPONSE;
import static org.springframework.jms.support.JmsHeaders.CORRELATION_ID;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix= "notification", name = "service", havingValue = "jms")
public class ConsumeFromProducerJMS {

  @Autowired
  JmsTemplate consumerJmsTemplate;

  @Transactional
  @JmsListener(destination = MESSAGE_QUEUE_REQUEST)
  public void receiveMessage(@Payload String payload,
      @Headers MessageHeaders headers,
      Message message, Session session) throws JMSException {

    log.info("##################################");
    log.info("ACTIVEMQ - CONSUMER : received message with correlation_id: <" + headers.get(CORRELATION_ID) + ">");
    log.info("##################################");

    String correlationId= (String) headers.get(CORRELATION_ID);

    consumerJmsTemplate.convertAndSend(MESSAGE_QUEUE_RESPONSE, "received Ok from Consumer", m -> {
      m.setJMSCorrelationID(correlationId);
      return m;
    });
  }
}
