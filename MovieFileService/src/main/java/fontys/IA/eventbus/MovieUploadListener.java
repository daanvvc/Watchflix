package fontys.IA.eventbus;

import fontys.IA.domain.MovieFile;
import fontys.IA.services.MovieFileService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieUploadListener {
    private MovieFileService movieFileService;
    private MessageSender messageSender;

    @RabbitListener(queues = "movie-upload-file-queue")
    public void addMovie(Message message) {
        String messageBody = new String(message.getBody());
        UUID movieId = UUID.fromString((String) message.getMessageProperties().getHeaders().get("movieId"));

        System.out.println(messageBody + "is being uploaded with movieId:" + movieId);

        // TODO Check if malicious file
        Resource file = new ClassPathResource("mockMovie.mp4");

        MovieFile movieFile = new MovieFile(movieId, file, messageBody);

        String upload_status = "SUCCEEDED";

        // Get the movie
        try {
            movieFileService.uploadMovieFile(movieFile);
        } catch (Exception ex) {
            upload_status = "FAILED";
        }

        messageSender.sendMessage("amq.topic", "movie-upload-status-routing-key", upload_status);
    }
}
