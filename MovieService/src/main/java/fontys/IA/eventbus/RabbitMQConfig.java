package fontys.IA.eventbus;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Makes a queue which listens to the user-forget-fanout
    @Bean
    public Queue movieForgetUserQueue() {
        return QueueBuilder.durable("movie-forget-user-queue").build();
    }

    @Bean
    public FanoutExchange forgetUserFanoutExchange() {
        return new FanoutExchange("user-forget-fanout");
    }

    @Bean
    public Binding movieBinding(FanoutExchange forgetUserFanoutExchange, Queue movieForgetUserQueue) {
        return BindingBuilder.bind(movieForgetUserQueue).to(forgetUserFanoutExchange);
    }
}

