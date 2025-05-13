package fontys.IA.repositories;


import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieRepository {
    Optional<Movie> findById(String MovieId);
    List<Movie> findNrOfMovies(int numberOfMovies);
    List<Movie> findAll();
    void save(Movie movie);
    void updateStatus(String movieId, Status uploadStatus);
    }
