package fontys.IA.repositories;

import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<UserRole> findRoleById(UUID id);
}