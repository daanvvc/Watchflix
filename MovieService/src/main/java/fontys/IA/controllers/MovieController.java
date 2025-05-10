package fontys.IA.controllers;

import fontys.IA.domain.Movie;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieName(@PathVariable("id") String movieId) {
        System.out.println("Accessed!");

        // Get the movie
        Movie movie = movieService.getMovie(movieId);

        if (movie == null) {
            System.out.println("This movie doesn't exist!");
            // TODO Which error?
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(movie.getName());
    }

    @GetMapping("/nr/{number}")
    public ResponseEntity<List<Movie>> getTenMovies(@PathVariable("number") int numberOfMovies) {
        System.out.println("Accessed! " + numberOfMovies);

        // Get the movies
        try{
            return ResponseEntity.ok(movieService.getNrOfMovies(numberOfMovies));
        } catch (IndexOutOfBoundsException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}