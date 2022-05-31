package fr.insee.pocasync;


import fr.insee.pocasync.consumer.broker.in.ConsumeFromProducerAMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
public class ConfigurationAMQP {

    public static final String MESSAGE_QUEUE_REQUEST = "message-queue-request";

    @Bean
    Queue queue() {
        return new Queue(MESSAGE_QUEUE_REQUEST, false);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ConsumeFromProducerAMQP receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(MESSAGE_QUEUE_REQUEST);
        container.setMessageListener(listenerAdapter);
        return container;
    }

}
