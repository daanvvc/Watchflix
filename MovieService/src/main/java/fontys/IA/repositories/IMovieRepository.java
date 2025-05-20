package fontys.IA.repositories;

import fontys.IA.domain.Movie;
import fontys.IA.domain.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieRepository extends MongoRepository<Movie, UUID> {
    @Query("{ 'id': ?0 }")
    @Update("{ '$set': { 'uploadStatus': ?1 } }")
    long updateStatus(UUID movieId, Status uploadStatus);

    List<Movie> findByUploadStatus(Status status, Pageable pageable);
}
