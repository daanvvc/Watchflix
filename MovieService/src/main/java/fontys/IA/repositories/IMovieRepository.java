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
    @Query("{ 'id': ?0, 'version': ?2 }")
    @Update("{ '$set': { 'uploadStatus': ?1 }, '$inc': { 'version': 1 } }")
    long updateStatus(UUID movieId, Status uploadStatus, long currentVersion);


    List<Movie> findByUploadStatus(Status status, Pageable pageable);
}
