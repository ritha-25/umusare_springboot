package innovation.ride.umusare.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Rabbitmq {

    public static final String QUEUE = "ride.events.queue";
    public static final String EXCHANGE = "ride.events.exchange";
    public static final String ROUTING_KEY = "ride.event";

    @Bean
    public Queue rideEventsQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange rideEventsExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue rideEventsQueue, DirectExchange rideEventsExchange) {
        return BindingBuilder.bind(rideEventsQueue).to(rideEventsExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}