package fontys.IA;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/movieFile")
public class MovieUploadRunner {

    private final RabbitTemplate rabbitTemplate;

    public MovieUploadRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestHeader("UUID") String movieId,
                                             @RequestParam("video") MultipartFile movieFile,
                                             @RequestParam("movieInformation") String movieInformationJson) {
        movieId = movieId.replaceAll("[\r\n]", "");
        UUID uuid = UUID.fromString(movieId);
        String sanitizedMovieId = uuid.toString();

        try {
            Message messageWithFile = MessageBuilder
                    .withBody(movieFile.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_BYTES)
                    .setHeader("movieId", sanitizedMovieId )
                    .build();

            Message messageWithInformation = MessageBuilder
                    .withBody(movieInformationJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                    .setHeader("movieId", sanitizedMovieId )
                    .build();

            rabbitTemplate.convertAndSend("amq.topic", "movie-file-upload-routing-key", messageWithFile);
            rabbitTemplate.convertAndSend("amq.topic", "movie-information-upload-routing-key", messageWithInformation);

            return ResponseEntity.ok("The upload request is being handled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload request was not able to be handled");
        }
    }
}
