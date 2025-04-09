package fontys.IA.domain;

import lombok.*;

import org.springframework.core.io.Resource;

@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
public class MovieFile {
    private long id;
    private Resource file;
    private String fileName;
}
