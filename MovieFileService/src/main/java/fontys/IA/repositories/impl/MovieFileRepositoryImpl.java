package fontys.IA.repositories.impl;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import fontys.IA.domain.MovieFile;
import fontys.IA.repositories.IMovieFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Repository
public class MovieFileRepositoryImpl implements IMovieFileRepository {

    private final BlobContainerClient containerClient;

    public MovieFileRepositoryImpl(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    @Override
    public void save(MovieFile movieFile) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(movieFile.getId().toString());

            InputStream inputStream = movieFile.getFile().getInputStream();
            long size = movieFile.getFile().contentLength();

            blobClient.upload(inputStream, size, true);

            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("video/mp4");
            blobClient.setHttpHeaders(headers);

            Map<String, String> metadata = new HashMap<>();
            metadata.put("UploaderId", movieFile.getUploaderId().toString());
            blobClient.setMetadata(metadata);

            System.out.println("Uploaded movie file: " + movieFile.getFileName());
        } catch (Exception ex) {
            System.out.print("Error uploading movie file" + ex);
            throw new RuntimeException("Failed to upload file to blob storage", ex);
        }
    }

    @Override
    public Optional<MovieFile> findById(String movieId) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(movieId);

            if (!blobClient.exists()) {
                return Optional.empty();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.download(outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            MovieFile movieFile = MovieFile.builder()
                    .id(UUID.fromString(movieId))
                    .fileName(blobClient.getBlobName())
                    .file(resource)
                    .build();

            return Optional.of(movieFile);
        } catch (Exception ex) {
            System.out.println("Error downloading movie file " + ex);
            return Optional.empty();
        }
    }

    @Override
    public void updateUploaderId(String oldUploaderId, String newUploaderId) {
        for (BlobItem blobItem : containerClient.listBlobs()) {
            BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());

            try {
                BlobProperties properties = blobClient.getProperties();
                Map<String, String> metadata = new HashMap<>(properties.getMetadata());

                // Only update if current uploaderId matches
                if (oldUploaderId.equals(metadata.get("UploaderId"))) {
                    metadata.put("uploaderId", newUploaderId);
                    blobClient.setMetadata(metadata);
                    System.out.println("Updated uploaderId for blob: " + blobItem.getName());
                }

            } catch (Exception ex) {
                System.err.println("Failed to update metadata for blob " + blobItem.getName() + ": " + ex.getMessage());
            }
        }
    }

    @Override
    public int countByUploaderId(String uploaderId) {
        int count = 0;

        for (BlobItem blobItem : containerClient.listBlobs()) {
            BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());

            try {
                BlobProperties properties = blobClient.getProperties();
                Map<String, String> metadata = properties.getMetadata();

                if (uploaderId.equals(metadata.get("UploaderId"))) {
                    count++;
                }

            } catch (Exception ex) {
                System.err.println("Failed to read metadata for blob " + blobItem.getName() + ": " + ex.getMessage());
            }
        }

        return count;
    }
}