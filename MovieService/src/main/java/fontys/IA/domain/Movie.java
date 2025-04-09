package fontys.IA.domain;

import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
public class Movie {
    private long id;
    private String name;
}
