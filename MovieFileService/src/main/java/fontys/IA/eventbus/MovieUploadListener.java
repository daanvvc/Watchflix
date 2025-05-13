package fontys.IA.eventbus;

import fontys.IA.domain.MovieFile;
import fontys.IA.services.MovieFileService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class MovieUploadListener {
    private MovieFileService movieFileService;
    private MessageSender messageSender;

    @RabbitListener(queues = "movie-upload-file-queue")
    public void addMovie(Message message) {
        String messageBody = new String(message.getBody());
        UUID movieId = UUID.fromString((String) message.getMessageProperties().getHeaders().get("movieId"));

        System.out.println("Uploading with movieId:" + movieId);

        // TODO Check if malicious file

        Resource movieResource = new ByteArrayResource(message.getBody());
        MovieFile movieFile = new MovieFile(movieId, movieResource, messageBody);

        String upload_status = "SUCCEEDED";
        // Get the movie
        try {
            movieFileService.uploadMovieFile(movieFile);
        } catch (Exception ex) {
            upload_status = "FAILED";
        }

        // Wait 10 seconds
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        Message messageWithStatus = MessageBuilder
                .withBody(upload_status.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("movieId", movieId)
                .build();

        messageSender.sendMessage("amq.topic", "movie-upload-status-routing-key", messageWithStatus);
    }
}
