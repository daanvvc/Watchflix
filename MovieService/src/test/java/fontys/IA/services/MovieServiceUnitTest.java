package fontys.IA.services;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import fontys.IA.repositories.IMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceUnitTest {
    @Mock
    private IMovieRepository movieMockRepository;
    @InjectMocks
    private MovieService movieService;

    Movie movie;
    UUID id = UUID.randomUUID();
    String name = "The Brutalist";
    @BeforeEach
    public void BeforeEach() {
        movie = new Movie(id, name, Status.PENDING);
    }

    @Test
    void CI_Test_SuccessTest() {
        assertTrue(true);
    }

    @Test
    // Successfully gets a movie from the mock repository
    void GetMovie_SuccessTest() {
        // Arrange
        Optional<Movie> returnMovie = Optional.of(movie);
        Movie expectedMovie = movie;

        when(movieMockRepository.findById(id)).thenReturn(returnMovie);

        // Act
        Movie actualMovie = movieService.getMovie(id.toString());

        // Assert
        assertEquals(actualMovie, expectedMovie);
        verify(movieMockRepository, times(1)).findById(id);
    }

    @Test
    // Gets a movie from the mock repository that does not exist
    void GetMovie_ThatDoesNotExist_SuccessTest() {
        // Arrange
        id = UUID.randomUUID();
        Optional<Movie> returnMovie = Optional.empty();

        when(movieMockRepository.findById(id)).thenReturn(returnMovie);

        // Act
        Movie actualMovie = movieService.getMovie(id.toString());

        // Assert
        assertNull(actualMovie);
        verify(movieMockRepository, times(1)).findById(id);
    }
}