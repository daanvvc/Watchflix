package fontys.IA.controllers;

import fontys.IA.domain.Movie;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMessageListener {
    MovieService movieService;
    MovieMessageSender movieMessageSender;

    // TODO OLD?
//    @RabbitListener(queues = "movie-queue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);

        // Get movie id from message
        int movieId;
        try {
            movieId = Integer.parseInt(message);
        } catch (NumberFormatException ex)
        {
            System.out.println("message wasn't an int!");
            return;
        }

        // Get the movie
        Movie movie = movieService.getMovie(movieId);

        if (movie == null) {
            System.out.println("This movie doesn't exist!");
            return;
        }

        movieMessageSender.sendMessage(movie);
    }
}