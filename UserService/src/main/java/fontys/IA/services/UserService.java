package fontys.IA.services;

import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private IUserRepository userRepository;

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

    public UserRole getRole(String userId) {
        Optional<UserRole> userRole = userRepository.findRoleById(UUID.fromString(userId));

        return userRole.orElse(null);
    }
}
