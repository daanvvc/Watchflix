package fontys.IA.eventbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import fontys.IA.services.MovieFileService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserForgetListener {
    private final RabbitTemplate rabbitTemplate;
    private final MovieFileService movieFileService;

    public UserForgetListener(RabbitTemplate rabbitTemplate, MovieFileService movieFileService) {
        this.rabbitTemplate = rabbitTemplate;
        this.movieFileService = movieFileService;
    }

    @RabbitListener(queues = "movie-file-forget-user-queue")
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
//            movieFileService.updateUploaderId(userId, anonymousUserId);

            isSuccessful = true;
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

