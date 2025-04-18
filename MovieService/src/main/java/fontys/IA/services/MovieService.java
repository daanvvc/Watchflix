package fontys.IA.services;

import fontys.IA.domain.Movie;
import fontys.IA.repositories.IMovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
    private IMovieRepository movieRepository;

    public Movie getMovie(long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        return movie.orElse(null);
    }

    public List<Movie> getNrOfMovies(int numberOfMovies) {
        if (numberOfMovies < 0 || numberOfMovies > 100) {
            throw new IndexOutOfBoundsException("This is too many or too few movies!");
        }

        return movieRepository.findNrOfMovies(numberOfMovies);
    }
}
