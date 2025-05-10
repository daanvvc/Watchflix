package fontys.IA.domain;

import lombok.*;

import org.springframework.core.io.Resource;

import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
public class MovieFile {
    private UUID id;
    private Resource file;
    private String fileName;
}
