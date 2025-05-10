package fontys.IA.eventbus;

import fontys.IA.domain.enums.Status;
import fontys.IA.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieUploadStatusUpdateListener {
    private MovieService movieService;

    @RabbitListener(queues="movie-upload-status-queue")
    public void changeMovieUploadStatus(String message) {
        Status status;
        switch (message){
            case "PENDING":
                status = Status.PENDING;
                break;
            case "SUCCEEDED":
                status = Status.SUCCEEDED;
                break;
            case "FAILED":
                status = Status.FAILED;
                break;
            default:
                // TODO Improve exception
                throw new IndexOutOfBoundsException();
        }

        System.out.println(status);

        // TODO get id as well
//        movieService.updateMovieUploadStatus(UUID.randomUUID().toString(), status);
    }
}
