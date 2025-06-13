package fontys.IA.eventbus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.IA.services.MovieService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserForgetListener {

    private final RabbitTemplate rabbitTemplate;

    private final MovieService movieService;

    public UserForgetListener(RabbitTemplate rabbitTemplate, MovieService movieService) {
        this.rabbitTemplate = rabbitTemplate;
        this.movieService = movieService;
    }

    @RabbitListener(queues = "movie-forget-user-queue")
    public void handleForgetUserMessage(Message message) throws IOException {
        // Get message information
        ObjectMapper mapperGet = new ObjectMapper();
        Map<String, String> payloadMap = mapperGet.readValue(message.getBody(), new TypeReference<>() {});
        String userId = payloadMap.get("userId");
        String anonymousUserId = payloadMap.get("anonymousUserId");

        System.out.println(userId + " " + anonymousUserId);

        // Anonymize the user
        boolean isSuccessful;
        try {
            isSuccessful = movieService.updateUploaderId(userId, anonymousUserId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            isSuccessful = false;
        }

        // Make a new message with correlation id
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("userId", userId);
        responsePayload.put("success", isSuccessful);

        MessageProperties props = new MessageProperties();
        props.setCorrelationId(message.getMessageProperties().getCorrelationId());
        Message responseMessage = new Message(new ObjectMapper().writeValueAsBytes(responsePayload), props);

        // Send the message
        System.out.println("Sending message: " + isSuccessful);
        rabbitTemplate.send("user-forget-response-queue", responseMessage);
    }
}

