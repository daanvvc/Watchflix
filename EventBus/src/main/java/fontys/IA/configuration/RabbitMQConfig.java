package fontys.IA.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("amq.topic");
    }

    @Bean
    public Queue movieUploadFileQueue() {
        return new Queue("movie-upload-file-queue");
    }

    @Bean
    public Queue movieUploadMetadataQueue() {
        return new Queue("movie-upload-information-queue");
    }

    @Bean
    public Queue movieUploadStatusQueue() {
        return new Queue("movie-upload-status-queue");
    }

    @Bean
    public Binding bindingA(TopicExchange topicExchange, Queue movieUploadFileQueue) {
        String routingKeyName = "movie-file-upload-routing-key";

        return BindingBuilder.bind(movieUploadFileQueue)
                .to(topicExchange)
                .with(routingKeyName);
    }

    @Bean
    public Binding bindingB(TopicExchange topicExchange, Queue movieUploadMetadataQueue) {
        String routingKeyName = "movie-information-upload-routing-key";

        return BindingBuilder.bind(movieUploadMetadataQueue)
                .to(topicExchange)
                .with(routingKeyName);
    }

    @Bean
    public Binding bindingC(TopicExchange topicExchange, Queue movieUploadStatusQueue) {
        String routingKeyName = "movie-upload-status-routing-key";

        return BindingBuilder.bind(movieUploadStatusQueue)
                .to(topicExchange)
                .with(routingKeyName);
    }

    // For scatter-gather (right to be forgotten)
    @Bean
    public FanoutExchange forgetUserFanoutExchange() {
        return new FanoutExchange("user-forget-fanout");
    }

    @Bean
    public Queue forgetUserResponseQueue() {
        return QueueBuilder.durable("user-forget-response-queue").build();
    }

    @Bean
    public Queue forgetUserFinalizedQueue() {
        return QueueBuilder.durable("user-forget-finalized-queue").build();
    }

    @Bean
    public Binding bindingD(TopicExchange topicExchange, Queue forgetUserFinalizedQueue) {
        String routingKeyName = "user-forget-finalized-routing-key";

        return BindingBuilder.bind(forgetUserFinalizedQueue)
                .to(topicExchange)
                .with(routingKeyName);
    }

    // Forces the queues to be binded
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationRunner runner(RabbitAdmin rabbitAdmin) {
        return args -> rabbitAdmin.initialize();
    }
}
