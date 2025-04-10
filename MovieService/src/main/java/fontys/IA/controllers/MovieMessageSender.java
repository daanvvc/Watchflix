package fontys.IA.controllers;

import fontys.IA.domain.Movie;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovieMessageSender {
    private final RabbitTemplate rabbitTemplate;

    public MovieMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Movie movie) {
        System.out.println("Sending message: " + movie.getName());
        rabbitTemplate.convertAndSend("amq.topic", "movie-routing-key", movie.getName());
    }
}