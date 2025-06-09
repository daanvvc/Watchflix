package fontys.IA.aggregator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForgetUserAggregator {
    private final Map<String, List<Boolean>> responseMap = new ConcurrentHashMap<>();

    private final RabbitTemplate rabbitTemplate;

    private static final int EXPECTED_RESPONSES = 2; // Currently: MovieService & MovieFileService

    public ForgetUserAggregator(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "user-forget-response-queue")
    public void handleResponse(Message message) throws IOException {
        ObjectMapper mapperGet = new ObjectMapper();
        Map<String, Object> payloadMap = mapperGet.readValue(message.getBody(), new TypeReference<>() {});

        // Get information from the message
        String userId = (String) payloadMap.get("userId");
        boolean success = (Boolean) payloadMap.get("success");
        String correlationId = message.getMessageProperties().getCorrelationId();

        // Adds the success, if no list was added for this correlation id, adds a new list
        responseMap.computeIfAbsent(correlationId, id -> Collections.synchronizedList(new ArrayList<>()))
                .add(success);

        System.out.println("Message received: " + success);

        // If all responses have been gotten
        if (responseMap.get(correlationId).size() >= EXPECTED_RESPONSES) {
            // If all were successful, this will be true, otherwise false
            boolean allSuccess = responseMap.get(correlationId).stream().allMatch(
                    wasDeletionSuccessful -> wasDeletionSuccessful);

            // Make the message to the finale queue
            Map<String, Object> responsePayload = new HashMap<>();
            responsePayload.put("userId", userId);
            responsePayload.put("success", allSuccess);
            Message resultMessage = new Message(new ObjectMapper().writeValueAsBytes(responsePayload));

            // Send the message
            System.out.println("Sending return message");
            rabbitTemplate.convertAndSend("amq.topic", "user-forget-finalized-routing-key",
                    resultMessage);

            // Clean up
            responseMap.remove(correlationId);
        }
    }
}
