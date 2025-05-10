package fontys.IA;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movieFile")
public class MovieUploadRunner {

    private final RabbitTemplate rabbitTemplate;

    public MovieUploadRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile() {
        try {
            rabbitTemplate.convertAndSend("amq.topic", "movie-upload-routing-key", "Hello from RabbitMQ!");
            return ResponseEntity.ok("The upload request is being handled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload request was not able to be handled");
        }
    }
}
