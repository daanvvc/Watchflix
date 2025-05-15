package fontys.IA.domain;

import fontys.IA.domain.enums.UserRole;
import lombok.*;

import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
public class User {
    UUID id;
    String email;
    String username;
    UserRole role;
}
