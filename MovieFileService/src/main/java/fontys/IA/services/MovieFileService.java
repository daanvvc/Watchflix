package fontys.IA.services;

import fontys.IA.domain.MovieFile;
import fontys.IA.repositories.IMovieFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieFileService {
    private IMovieFileRepository movieFileRepository;

    public MovieFile getMovieFile(String movieId) {
        Optional<MovieFile> movie = movieFileRepository.findById(movieId);

        return movie.orElse(null);
    }

    public void uploadMovieFile(MovieFile movieFile) {
        movieFileRepository.save(movieFile);
    }
}
