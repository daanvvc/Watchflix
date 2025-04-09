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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public class MovieFileRepositoryFakeImpl implements IMovieFileRepository {
    private List<MovieFile> movieFiles;

    public MovieFileRepositoryFakeImpl() {
        movieFiles = new ArrayList<>();

        Resource file = new ClassPathResource("mockMovie.mp4");

        movieFiles.add(new MovieFile(0, file, "The_Brutalist.mp4"));
        movieFiles.add(new MovieFile(1, file, "Dune_Part_Two.mp4"));
        movieFiles.add(new MovieFile(2, file, "Challengers.mp4"));
        movieFiles.add(new MovieFile(3, file, "Anora.mp4"));
        movieFiles.add(new MovieFile(4, file, "Nosferatu.mp4"));
        movieFiles.add(new MovieFile(5, file, "Memoir_of_a_Snail.mp4"));
        movieFiles.add(new MovieFile(6, file, "La_La_Land.mp4"));
        movieFiles.add(new MovieFile(7, file, "Only_Yesterday.mp4"));
        movieFiles.add(new MovieFile(8, file, "Everything_Everywhere_All_At_Once.mp4"));
        movieFiles.add(new MovieFile(9, file, "Poor_Things.mp4"));
        movieFiles.add(new MovieFile(10, file, "Lion_King.mp4"));
        movieFiles.add(new MovieFile(11, file, "Lost_in_Translation.mp4"));
        movieFiles.add(new MovieFile(12, file, "The_Most_Precious_of_Cargoes.mp4"));
        movieFiles.add(new MovieFile(13, file, "The_Eternal_Sunshine_of_the_Spotless_Mind.mp4"));
        movieFiles.add(new MovieFile(14, file, "Dial_M_for_Murder.mp4"));
        movieFiles.add(new MovieFile(15, file, "Tokyo_Story.mp4"));
    }

    public Optional<MovieFile> findById(long movieId) {
        for (MovieFile movieFile : movieFiles) {
            if (movieFile.getId() == movieId) {
                return Optional.of(movieFile);
            }
        }

        return Optional.empty();
    }

    public List<MovieFile> findNrOfMovies(int numberOfMovies) {
        Collections.shuffle(movieFiles);

        return movieFiles.subList(0, numberOfMovies);
    }
}
