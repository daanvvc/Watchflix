package fontys.IA.eventbus;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieUploadListener {
    private MovieService movieService;

    @RabbitListener(queues = "movie-upload-metadata-queue")
    public void addMovie(Message message) {
        String messageBody = new String(message.getBody());
        UUID movieId = UUID.fromString((String) message.getMessageProperties().getHeaders().get("movieId"));

        System.out.println(messageBody + "is being uploaded with movieId:" + movieId);

        movieService.addMovie(new Movie(movieId, messageBody, Status.PENDING));
    }
}