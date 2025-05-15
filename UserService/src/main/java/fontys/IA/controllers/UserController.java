package fontys.IA.controllers;

import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    UserService userService;
    AESEncrypter aesEncrypter;

    @PostMapping("/user")
    public void addUser(User user) {
        User newUser = new User(UUID.randomUUID(), user.getEmail(), user.getUsername(), UserRole.USER);

        userService.addUser(newUser);
    }

    @PostMapping("/admin")
    public void addAdmin(User admin) {
        User newAdmin = new User(UUID.randomUUID(), admin.getEmail(), admin.getUsername(), UserRole.ADMIN);

        userService.addUser(newAdmin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String userId) {
        // Get the user
        User user = userService.getUser(userId);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(user);
    }
}
