package fr.insee.pocasync.producer.broker.in;

import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "jms")
@Component
public class ResponseFromConsumerJMS {

    private final JmsTemplate jmsTemplate;
    private final UserRepository userRepository;

    @Transactional
    public void receiveResponse(String destination, String jmsCorrelationId) {

        String response = (String) jmsTemplate.receiveSelectedAndConvert(
                destination,
                "JMSCorrelationID = '" + jmsCorrelationId + "'");

        if (response != null) {

            log.info("##################################");
            log.info("ACTIVEMQ - PRODUCER : Ok from consumer for correlation_id <" + jmsCorrelationId + ">");
            log.info("##################################");

            UserDTO user = userRepository.findByCorrelationId(UUID.fromString(jmsCorrelationId));
            user.setRegistered(true);
            userRepository.save(user);
        }
    }
}
