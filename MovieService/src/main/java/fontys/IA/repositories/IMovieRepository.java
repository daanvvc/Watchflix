package fontys.IA.repositories;


import fontys.IA.domain.Movie;

import java.util.List;
import java.util.Optional;

public interface IMovieRepository {
    Optional<Movie> findById(long MovieId);
    List<Movie> findNrOfMovies(int numberOfMovies);
    }
