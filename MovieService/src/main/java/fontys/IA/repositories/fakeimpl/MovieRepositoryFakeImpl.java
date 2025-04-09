package fontys.IA.repositories.fakeimpl;

import fontys.IA.domain.Movie;
import fontys.IA.repositories.IMovieRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepositoryFakeImpl implements IMovieRepository {
    private List<Movie> movies;

    public MovieRepositoryFakeImpl() {
        movies = new ArrayList<>();

        movies.add(new Movie(0, "The Brutalist"));
        movies.add(new Movie(1, "Dune: Part Two"));
        movies.add(new Movie(2, "Challengers"));
        movies.add(new Movie(3, "Anora"));
        movies.add(new Movie(4, "Nosferatu"));
        movies.add(new Movie(5, "Memoir of a Snail"));
        movies.add(new Movie(6, "La La Land"));
        movies.add(new Movie(7, "Only Yesterday"));
        movies.add(new Movie(8, "Everything Everywhere All At Once"));
        movies.add(new Movie(9, "Poor Things"));
        movies.add(new Movie(10, "Lion King"));
        movies.add(new Movie(11, "Lost in Translation"));
        movies.add(new Movie(12, "The Most Precious of Cargoes"));
        movies.add(new Movie(13, "The Eternal Sunshine of the Spotless Mind"));
        movies.add(new Movie(14, "Dial M for Murder"));
        movies.add(new Movie(15, "Tokyo Story"));
    }

    public Optional<Movie> findById(long movieId) {
        for (Movie movie : movies) {
            if (movie.getId() == movieId) {
                return Optional.of(movie);
            }
        }

        return Optional.empty();
    }

    public List<Movie> findNrOfMovies(int numberOfMovies) {
        Collections.shuffle(movies);

        return movies.subList(0, numberOfMovies);
    }
}
