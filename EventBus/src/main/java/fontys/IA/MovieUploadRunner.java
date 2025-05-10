package fontys.IA;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movieFile")
public class MovieUploadRunner {

    private final RabbitTemplate rabbitTemplate;

    public MovieUploadRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestHeader("UUID") String movieId) {
        try {
            Message message = MessageBuilder
                    .withBody("Hello from RabbitMQ!".getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                    .setHeader("movieId", movieId)
                    .build();

            rabbitTemplate.convertAndSend("amq.topic", "movie-upload-routing-key", message);
            return ResponseEntity.ok("The upload request is being handled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload request was not able to be handled");
        }
    }
}
