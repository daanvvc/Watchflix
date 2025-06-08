package fontys.IA;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/movieFile")
public class MovieUploadRunner {
    private final RabbitTemplate rabbitTemplate;
    private final WebClient webClient;

    public MovieUploadRunner(RabbitTemplate rabbitTemplate, WebClient.Builder builder) {
        this.rabbitTemplate = rabbitTemplate;
        this.webClient = builder.baseUrl("http://USER-SERVICE").build();
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestHeader("X-User-Id") String userId,
                                             @RequestHeader("UUID") String movieId,
                                             @RequestParam("video") MultipartFile movieFile,
                                             @RequestParam("movieInformation") String movieInformationJson) {
        // Get the role of the user who is uploading this movie
         String role = webClient.get()
                .uri("/user/role/{id}", userId)
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(String.class).block();

        System.out.println("This is a: " + role);

        // Check that the user who requests this is an admin
        if (role == null) {
            return ResponseEntity.internalServerError().build();
        } else if (role.equals("USER")) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (!role.equals("ADMIN")) {
            return ResponseEntity.internalServerError().build();
        }

        movieId = movieId.replaceAll("[\r\n]", "");
        UUID uuid = UUID.fromString(movieId);
        String sanitizedMovieId = uuid.toString();

        try {
            Message messageWithFile = MessageBuilder
                    .withBody(movieFile.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_BYTES)
                    .setHeader("movieId", sanitizedMovieId )
                    .setHeader("userId", userId)
                    .build();

            Message messageWithInformation = MessageBuilder
                    .withBody(movieInformationJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                    .setHeader("movieId", sanitizedMovieId )
                    .setHeader("userId", userId)
                    .build();

            rabbitTemplate.convertAndSend("amq.topic",
                    "movie-file-upload-routing-key",
                    messageWithFile);
            rabbitTemplate.convertAndSend("amq.topic",
                    "movie-information-upload-routing-key",
                    messageWithInformation);

            return ResponseEntity.ok("The upload request is being handled");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload request was not able to be handled");
        }
    }
}
