package fontys.IA.repositories.fakeimpl;

import fontys.IA.domain.MovieFile;
import fontys.IA.repositories.IMovieFileRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import java.net.MalformedURLException;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Repository
public class MovieFileRepositoryFakeImpl implements IMovieFileRepository {
    private List<MovieFile> movieFiles;

    public MovieFileRepositoryFakeImpl() {
        movieFiles = new ArrayList<>();

        Resource file = new ClassPathResource("mockMovie.mp4");

        movieFiles.add(new MovieFile(UUID.randomUUID(), file, "The_Brutalist.mp4"));
    }

    public Optional<MovieFile> findById(String movieId) {
        return Optional.of(movieFiles.get(0));
    }

    public List<MovieFile> findNrOfMovies(int numberOfMovies) {
        Collections.shuffle(movieFiles);

        return movieFiles.subList(0, numberOfMovies);
    }
}
