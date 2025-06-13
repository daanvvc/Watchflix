package fontys.IA.repositories;


import fontys.IA.domain.MovieFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieFileRepository {
    Optional<MovieFile> findById(String MovieId);
    void save(MovieFile movieFile);
    void updateUploaderId(String oldUploaderId, String newUploaderId);
    int countByUploaderId(String uploaderId);
}
