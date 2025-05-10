package fontys.IA.repositories;


import fontys.IA.domain.MovieFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieFileRepository {
    Optional<MovieFile> findById(String MovieId);
    List<MovieFile> findNrOfMovies(int numberOfMovies);
    }
