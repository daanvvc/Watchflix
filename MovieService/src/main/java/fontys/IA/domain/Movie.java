package fontys.IA.domain;

import fontys.IA.domain.enums.Status;
import lombok.*;

import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
public class Movie {
    private UUID id;
    private String name;
    private Status uploadStatus;

    public void updateUploadStatus(Status updatedUploadStatus) {
        uploadStatus = updatedUploadStatus;
    }
}
