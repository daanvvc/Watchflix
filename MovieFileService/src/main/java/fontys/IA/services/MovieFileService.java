package fontys.IA.services;

import fontys.IA.domain.MovieFile;
import fontys.IA.repositories.IMovieFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieFileService {
    private IMovieFileRepository movieFileRepository;
    private SafeUploadService safeUploadService;

    public MovieFile getMovieFile(String movieId) {
        Optional<MovieFile> movie = movieFileRepository.findById(movieId);

        return movie.orElse(null);
    }

    public void uploadMovieFile(MovieFile movieFile) throws IOException {
        try {
            System.out.println("Starting...");

            // Multiple checks to see if the file is malicious
            if (!safeUploadService.isValidMp4WithH264(movieFile.getFile())) {
                throw new SecurityException("This is not a MP4 with H.264 video");
            }

            if (!safeUploadService.hasValidFileName(movieFile.getFileName())) {
                throw new SecurityException("This title is too long!");
            }

            if (!safeUploadService.isMaliciousVirusTotalScan(movieFile)) {
                throw new SecurityException("File failed security scan");
            }

            System.out.println("Security scan is ok");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new IOException(ex.getMessage());
        }

        movieFileRepository.save(movieFile);
    }

    public void updateUploaderId(String oldUploaderId, String newUploaderId) {
        movieFileRepository.updateUploaderId(oldUploaderId, newUploaderId);
    }
}
