package fontys.IA.eventbus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.IA.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class ForgetUserListener {
    private final UserService userService;

    @RabbitListener(queues = "user-forget-finalized-queue")
    public void handleForgetUserResult(Message message) throws IOException {
        ObjectMapper mapperGet = new ObjectMapper();
        Map<String, Object> payloadMap = mapperGet.readValue(message.getBody(), new TypeReference<>() {});

        String userId = (String) payloadMap.get("userId");
        boolean success = (Boolean) payloadMap.get("success");

        System.out.println("ForgetUser result for userId " + userId + ": " + success);

        try {
        userService.tryToDelete(userId, success);
        } catch (Exception ex) {
            // TODO add to logger
        }
    }
}
