package fontys.IA.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class UserService {
    private IUserRepository userRepository;
    private SupabaseService supabaseService;
    private final ObjectMapper objectMapper;

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

    public void deleteUser(String userId) {
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
            System.out.println("Failed to delete user from Neon database with ID " + user.getId());
            throw new IllegalStateException("Failed to delete user from Neon database with ID " + user.getId());
        }

        // Delete the user from Supabase
        if (!supabaseService.deleteUser(userId)) {
            System.out.println("Failed to delete user from Supabase database with ID " + user.getId());
            throw new IllegalStateException("Failed to delete user from Supabase database with ID " + user.getId());
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
