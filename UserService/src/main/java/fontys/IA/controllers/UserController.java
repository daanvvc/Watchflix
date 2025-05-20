package fontys.IA.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.services.UserService;
import lombok.AllArgsConstructor;
import org.bouncycastle.crypto.OutputLengthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    UserService userService;
    AESEncrypter aesEncrypter;

    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestParam("user") String userJson) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> infoMap = objectMapper.readValue(new String(userJson.getBytes()), Map.class);

            UUID userId = UUID.fromString((String) infoMap.get("id"));
            String email = (String) infoMap.get("email");
            String username = email.split("@")[0];

            User user = new User(userId, email, username, UserRole.USER);

            userService.addUser(user);

            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Void> addAdmin(@RequestHeader("X-User-Id") String userId, User admin) {
        // Check that the user who is adding the admin is also an admin
        if (userService.getUser(userId).getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User newAdmin = new User(UUID.randomUUID(), admin.getEmail(), admin.getUsername(), UserRole.ADMIN);

        userService.addUser(newAdmin);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@RequestHeader("X-User-Id") String userIdRequester,
                                        @PathVariable("id") String requestedUserId) {
        // Check that the user who is requesting user data is authorized to do so
        // (User can only get their own data)
        if (!userIdRequester.equals(requestedUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get the user
        User user = userService.getUser(requestedUserId);

        UserRole userRole = userService.getRole(requestedUserId);

        System.out.println(userRole);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<String> getRole(@RequestHeader("X-User-Id") String userIdRequester,
                                        @PathVariable("id") String requestedUserId) {
        // Check that the user who is requesting user data is authorized to do so
        // (User can only get their own data)
        if (!userIdRequester.equals(requestedUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get the user
        UserRole userRole = userService.getRole(requestedUserId);

        if (userRole == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userRole.toString());
    }
}
