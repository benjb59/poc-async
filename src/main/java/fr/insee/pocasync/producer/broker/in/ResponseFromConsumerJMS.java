package fr.insee.pocasync.producer.broker.in;

import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@Component
public class ResponseFromConsumerJMS {

    private final JmsTemplate jmsTemplate;
    private final UserRepository userRepository;

    public void receiveResponse(String destination, String jmsCorrelationId) {

        String response = (String) jmsTemplate.receiveSelectedAndConvert(
                destination,
                "JMSCorrelationID = '" + jmsCorrelationId + "'");

        if (response != null) {

            log.info("##################################");
            log.info("ACTIVEMQ - PRODUCER : Ok from consumer for correlation_id <" + jmsCorrelationId + ">");
            log.info("##################################");

            userRepository.findByCorrelationId(UUID.fromString(jmsCorrelationId)).subscribe(
                    userDTO -> userRepository.setRegisteredForUserId(userDTO.userId(), true).subscribe(n->log.info("Modifying : "+n))
            );
        }
    }
}
