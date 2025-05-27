package fontys.IA.controllers;

import fontys.IA.domain.MovieFile;
import fontys.IA.services.MovieFileService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
    public ResponseEntity<Resource> getMovieFile(@PathVariable("id") String movieId) {
        System.out.println("Accessed!");

        // Get the movie
        MovieFile movieFile = movieFileService.getMovieFile(movieId);

        System.out.println("Movie was gotten");
        System.out.println("File exists: " + movieFile.getFile().exists());
        System.out.println("Can read: " + movieFile.getFile().isReadable());

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

    // Upload endpoint for testing
    @PostMapping(value = "/upload")
    public void uploadFile(@RequestParam("video") MultipartFile file) throws IOException {
        try {
            MovieFile movieFile = new MovieFile(UUID.randomUUID(), file.getResource(), "Blob_Test");

            movieFileService.uploadMovieFile(movieFile);

        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}