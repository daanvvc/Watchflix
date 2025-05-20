package fontys.IA.services;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.repositories.IMovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieService {
    private IMovieRepository movieRepository;

    public Movie getMovie(String movieId) {
        Optional<Movie> movie = movieRepository.findById(UUID.fromString(movieId));

        return movie.orElse(null);
    }

    public List<Movie> getNrOfMovies(int numberOfMovies) {
        if (numberOfMovies < 0 || numberOfMovies > 100) {
            throw new IndexOutOfBoundsException("This is too many or too few movies!");
        }

        Pageable limit = PageRequest.of(0, numberOfMovies);

        List<Movie> movies = movieRepository.findByUploadStatus(Status.SUCCEEDED, limit);
        Collections.reverse(movies);
        return movies;
    }

    public void addMovie(Movie movie) {
        if(movieRepository.findById(movie.getId()).isPresent()){
            // TODO throw exception?
        }

        movieRepository.save(movie);
    }

    public List<Movie> getAll() {
        List<Movie> movies = movieRepository.findAll();
        Collections.reverse(movies);
        return movies;
    }

    public void updateMovieUploadStatus(String movieId, Status uploadStatus) {
        if(movieRepository.findById(UUID.fromString(movieId)).isPresent()){
            // TODO throw exception?
        }

        movieRepository.updateStatus(UUID.fromString(movieId), uploadStatus);
    }
}
