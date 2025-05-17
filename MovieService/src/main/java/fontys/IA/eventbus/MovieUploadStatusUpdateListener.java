package fontys.IA.eventbus;

import fontys.IA.domain.enums.Status;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieUploadStatusUpdateListener {
    private MovieService movieService;

    @RabbitListener(queues="movie-upload-status-queue")
    public void changeMovieUploadStatus(Message message) {
        String movieId = (String) message.getMessageProperties().getHeaders().get("movieId");

        Status status = switch (new String(message.getBody())) {
            case "PENDING" -> Status.PENDING;
            case "SUCCEEDED" -> Status.SUCCEEDED;
            case "SECURITY_FAIL" -> Status.SECURITY_FAIL;
            case "OTHER_FAIL" -> Status.OTHER_FAIL;
            default ->
                // TODO Improve exception
                throw new IndexOutOfBoundsException();
        };

        System.out.println(movieId + ": " + status);

        movieService.updateMovieUploadStatus(movieId, status);
    }
}
