package fontys.IA.services;

import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private IUserRepository userRepository;

    public void addUser(User user) {
        if(userRepository.findById(user.getId().toString()).isPresent()){
            // TODO throw exception?
        }

        String email = AESEncrypter.encrypt(user.getEmail());
        User newUser = new User(user.getId(), email, user.getUsername(), user.getRole());

        userRepository.save(newUser);
    }

    public User getUser(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        String email = AESEncrypter.decrypt(user.get().getEmail());
        User returnUser = new User(user.get().getId(), email, user.get().getUsername(), user.get().getRole());

        return returnUser;
    }
}
