package fontys.IA.controllers;

import fontys.IA.domain.MovieFile;
import fontys.IA.services.MovieFileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/movieFile")
public class MovieFileController {
    private MovieFileService movieFileService;

    @GetMapping("/healthcheck")
    public ResponseEntity<Void> HealthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getMovieFile(@PathVariable("id") int movieId) {
        System.out.println("Accessed!");

        // Get the movie
        MovieFile movieFile = movieFileService.getMovieFile(movieId);

        if (movieFile == null) {
            System.out.println("This movie doesn't exist!");
            // TODO Which error?
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + movieFile.getFileName() + "\"")
                .body(movieFile.getFile());
    }
}