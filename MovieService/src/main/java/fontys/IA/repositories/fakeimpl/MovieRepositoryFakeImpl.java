package fontys.IA.repositories.fakeimpl;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.repositories.IMovieRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MovieRepositoryFakeImpl implements IMovieRepository {
    private List<Movie> movies;

    public MovieRepositoryFakeImpl() {
        movies = new ArrayList<>();

        movies.add(new Movie(UUID.randomUUID(), "The Brutalist", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Dune: Part Two", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Challengers", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Anora", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Nosferatu", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Memoir of a Snail", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "La La Land", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Only Yesterday", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Everything Everywhere All At Once", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Poor Things", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Lion King", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Lost in Translation", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "The Most Precious of Cargoes", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "The Eternal Sunshine of the Spotless Mind", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Dial M for Murder", Status.SUCCEEDED));
        movies.add(new Movie(UUID.randomUUID(), "Tokyo Story", Status.SUCCEEDED));
    }

    public Optional<Movie> findById(String movieId) {
        for (Movie movie : movies) {
            if (movie.getId().equals(movieId)) {
                return Optional.of(movie);
            }
        }

        return Optional.empty();
    }

    public List<Movie> findNrOfMovies(int numberOfMovies) {
        Collections.shuffle(movies);

        return movies.subList(0, numberOfMovies);
    }

    public void save(Movie movie) {
        movies.add(movie);
    }

    public void updateStatus(String movieId, Status uploadStatus) {
        Movie movie = findById(movieId).get();
        movie.updateUploadStatus(uploadStatus);
    }
}
