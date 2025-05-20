package fontys.IA.repositories.fakeimpl;

import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.repositories.IUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryImpl extends JpaRepository<User, UUID>, IUserRepository {
    @Query("SELECT u.role FROM User u WHERE u.id = :id")
    Optional<UserRole> findRoleById(@Param("id") UUID id);
}
