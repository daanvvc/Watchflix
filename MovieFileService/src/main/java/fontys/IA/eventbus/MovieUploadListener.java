package fontys.IA.eventbus;

import fontys.IA.domain.MovieFile;
import fontys.IA.services.MovieFileService;
import lombok.AllArgsConstructor;
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

    @RabbitListener(queues = "movie-upload-queue")
    public void addMovie(String message) {
        System.out.println(message + "is being uploaded");

        // TODO Check if malicious file

        // TODO get id as well

        Resource file = new ClassPathResource("mockMovie.mp4");

        MovieFile movieFile = new MovieFile(UUID.randomUUID(), file, message);

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
