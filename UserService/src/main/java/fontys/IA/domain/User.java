package fontys.IA.domain;

import fontys.IA.domain.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    UUID id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String username;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    UserRole role;
}
