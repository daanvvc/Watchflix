package fontys.IA.services;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.repositories.IMovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieService {
    private IMovieRepository movieRepository;

    public Movie getMovie(String movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        return movie.orElse(null);
    }

    public List<Movie> getNrOfMovies(int numberOfMovies) {
        if (numberOfMovies < 0 || numberOfMovies > 100) {
            throw new IndexOutOfBoundsException("This is too many or too few movies!");
        }

        return movieRepository.findNrOfMovies(numberOfMovies);
    }

    public void addMovie(Movie movie) {
        if(movieRepository.findById(movie.getId().toString()).isPresent()){
            // TODO throw exception?
        }

        movieRepository.save(movie);
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public void updateMovieUploadStatus(String movieId, Status uploadStatus) {
        if(movieRepository.findById(movieId).isPresent()){
            // TODO throw exception?
        }


        movieRepository.updateStatus(movieId, uploadStatus);
    }
}
