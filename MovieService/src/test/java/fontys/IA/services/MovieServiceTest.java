package fontys.IA.services;

import fontys.IA.domain.Movie;
import fontys.IA.repositories.IMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private IMovieRepository movieMockRepository;
    @InjectMocks
    private MovieService movieService;

    Movie movie;
    String id = UUID.randomUUID().toString();
    String name = "The Brutalist";
    @BeforeEach
    public void BeforeEach() {
        movie = new Movie(id, name);
    }

    @Test
    void CI_Test_SuccessTest() {
        assertTrue(true);
    }

    @Test
    void GetMovie_SuccessTest() {
        // Arrange
        Optional<Movie> returnMovie = Optional.of(movie);
        Movie expectedMovie = movie;

        when(movieMockRepository.findById(id)).thenReturn(returnMovie);

        // Act
        Movie actualMovie = movieService.getMovie(id);

        // Assert
        assertEquals(actualMovie, expectedMovie);
        verify(movieMockRepository, times(1)).findById(id);
    }

    @Test
    void GetMovie_ThatDoesNotExist_SuccessTest() {
        // Arrange
        id = UUID.randomUUID().toString();
        Optional<Movie> returnMovie = Optional.empty();

        when(movieMockRepository.findById(id)).thenReturn(returnMovie);

        // Act
        Movie actualMovie = movieService.getMovie(id);

        // Assert
        assertNull(actualMovie);
        verify(movieMockRepository, times(1)).findById(id);
    }
}