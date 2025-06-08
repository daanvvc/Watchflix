package fontys.IA.eventbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieUploadListener {
    private MovieService movieService;

    @RabbitListener(queues = "movie-upload-information-queue")
    public void addMovie(Message message) {
        // Get the movieId
        UUID movieId = UUID.fromString((String) message.getMessageProperties().getHeaders().get("movieId"));
        UUID userId = UUID.fromString((String) message.getMessageProperties().getHeaders().get("userId"));

        try {
            // Get the movie information
            String movieInformationJson = new String(message.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> infoMap = objectMapper.readValue(movieInformationJson, Map.class);
            String title = (String) infoMap.get("name");

            System.out.println(title + "is being uploaded with movieId:" + movieId);

            movieService.addMovie(new Movie(movieId, title, Status.PENDING, userId));
        } catch (Exception ex) {
            // TODO Improve
            return;
        }
    }
}