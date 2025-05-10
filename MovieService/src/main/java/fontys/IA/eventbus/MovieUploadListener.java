package fontys.IA.eventbus;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieUploadListener {
    private MovieService movieService;

    @RabbitListener(queues = "movie-upload-metadata-queue")
    public void addMovie(String message) {
        System.out.println(message + "is being uploaded");

        // TODO get id as well
        Movie movie = new Movie(UUID.randomUUID(), message, Status.PENDING);

        movieService.addMovie(movie);
    }
}
