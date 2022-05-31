package fr.insee.pocasync;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "amqp")
public class ConfigurationAMQP {

    public static final String EXCHANGE_NAME = "spring-boot-exchange";
    public static final String MESSAGE_QUEUE_REQUEST = "message-queue-request";
    public static final String ROUTING_KEY = "foo.bar.#";

    @Bean
    Queue queue() {
        return new Queue(MESSAGE_QUEUE_REQUEST, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate){
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
