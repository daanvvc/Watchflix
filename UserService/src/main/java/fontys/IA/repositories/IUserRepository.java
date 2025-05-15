package fontys.IA.repositories;

import fontys.IA.domain.User;

import java.util.Optional;

public interface IUserRepository {
    void save(User user);
    Optional<User> findById(String userId);
}
