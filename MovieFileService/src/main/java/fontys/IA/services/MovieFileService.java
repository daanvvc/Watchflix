package fontys.IA.services;

import fontys.IA.domain.MovieFile;
import fontys.IA.repositories.IMovieFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieFileService {
    private IMovieFileRepository movieFileRepository;

    public MovieFile getMovieFile(long movieId) {
        Optional<MovieFile> movie = movieFileRepository.findById(movieId);

        return movie.orElse(null);
    }
}
