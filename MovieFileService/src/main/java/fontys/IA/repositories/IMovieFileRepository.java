package fontys.IA.repositories;


import fontys.IA.domain.MovieFile;

import java.util.List;
import java.util.Optional;

public interface IMovieFileRepository {
    Optional<MovieFile> findById(long MovieId);
    List<MovieFile> findNrOfMovies(int numberOfMovies);
    }
