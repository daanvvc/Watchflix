package fontys.IA.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.eventbus.MessageSender;
import fontys.IA.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class UserService {
    private IUserRepository userRepository;
    private SupabaseService supabaseService;
    private final ObjectMapper objectMapper;
    private MessageSender messageSender;

    public void addUser(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            // TODO throw exception?
        }

        String email = AESEncrypter.encrypt(user.getEmail());
        User newUser = new User(user.getId(), email, user.getUsername(), user.getRole());

        userRepository.save(newUser);
    }

    public User getUser(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        if (user.isEmpty()) {
            return null;
        }

        String email = AESEncrypter.decrypt(user.get().getEmail());
        User returnUser = new User(user.get().getId(), email, user.get().getUsername(), user.get().getRole());

        return returnUser;
    }

    public UserRole getRole(String userId){
        Optional<UserRole> userRole = userRepository.findRoleById(UUID.fromString(userId));

        return userRole.orElse(null);
    }

    public void sendDeletionUserMessage(String userId) throws JsonProcessingException {
        System.out.println("Starting to delete: " + userId);

        // Get the user
        User user = getUser(userId);

        // Check the user is not null
        if (user == null) {
            throw new NullArgumentException("User cannot be null");
        }

        String anonymousId = userRepository.findByUsername("anonymous").get().getId().toString();

        // Make the message with user id, the id to make it anonymous and a correlation id
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("userId", userId);
        payloadMap.put("anonymousUserId", anonymousId);

        MessageProperties props = new MessageProperties();
        props.setCorrelationId(UUID.randomUUID().toString());
        Message message = new Message(new ObjectMapper().writeValueAsBytes(payloadMap), props);

        System.out.println("Sending message...");

        // Send the message
        messageSender.sendMessage("user-forget-fanout", "", message);
    }

    public void tryToDelete(String userId, boolean deletedInMicroServices) {
        // Check that the user was deleted or anonymized in the microservices
        if (!deletedInMicroServices) {
            System.out.println("Failed to delete user from micro serviceswith ID " + userId);
            throw new IllegalStateException("Failed to delete user from microservices with ID " + userId);
        }

        // Get the user
        User user = getUser(userId);

        // Check the user is not null
        if (user == null) {
            throw new NullArgumentException("User cannot be null");
        }

        // Delete the user in the user repository
        userRepository.delete(user);

        // Check that the user was deleted
        if(userRepository.existsById(user.getId())) {
            System.out.println("Failed to delete user from Neon database with ID " + userId);
            throw new IllegalStateException("Failed to delete user from Neon database with ID " + userId);
        }

        // Delete the user from Supabase
        if (!supabaseService.deleteUser(userId)) {
            System.out.println("Failed to delete user from Supabase database with ID " + userId);
            throw new IllegalStateException("Failed to delete user from Supabase database with ID " + userId);
        }
    }

    // Should return a zip file
    public File rightOfAccess(String userId) throws IOException  {
        // Get data from Supabase
        JsonNode supabaseUserInfo = supabaseService.getUserInformation(userId);

        // Then, get the user
        User user = getUser(userId);

        // Check the user is not null
        if (user == null) {
            throw new NullArgumentException("User cannot be null");
        }

        // Create temp directory
        Path tempDir = Files.createTempDirectory("user_export_");

        // Write JSON files with user information
        Path supabaseFile = tempDir.resolve("supabase_user.json");
        Path userServiceFile = tempDir.resolve("user_service.json");

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(supabaseFile.toFile(), supabaseUserInfo);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(userServiceFile.toFile(), user);

        // Create the file
        Path zipFilePath = tempDir.resolve("gdpr-user-data.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            addFileToZip(zipOut, supabaseFile, "supabase_user.json");
            addFileToZip(zipOut, userServiceFile, "user_service.json");
        }

        return zipFilePath.toFile();
    }

    private void addFileToZip(ZipOutputStream zipOut, Path filePath, String entryName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }

            zipOut.closeEntry();
        }
    }
}
