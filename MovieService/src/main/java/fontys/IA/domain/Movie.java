package fontys.IA.domain;

import fontys.IA.domain.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "movies")
@Data
@Setter(AccessLevel.NONE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    private UUID id;
    private String name;
    private Status uploadStatus;
    @Version
    private Long version;

    public Movie(UUID id, String name, Status uploadStatus) {
        this.id = id;
        this.name = name;
        this.uploadStatus = uploadStatus;
    }

    public void updateUploadStatus(Status updatedUploadStatus) {
        uploadStatus = updatedUploadStatus;
    }
}
