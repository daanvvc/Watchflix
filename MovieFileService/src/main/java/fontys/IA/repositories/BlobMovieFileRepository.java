package fontys.IA.repositories;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Repository
public class BlobMovieFileRepository {

    private final BlobContainerClient containerClient;

    public BlobMovieFileRepository(@Value("${azure.storage.connection-string}") String connectionString,
                                   @Value("${azure.storage.container-name}") String containerName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);

        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    public void upload(String fileName, InputStream data, long size, String contentType) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
        blobClient.upload(data, size, true);
        blobClient.setHttpHeaders(headers);
    }

    public InputStream download(String fileName) {
        return containerClient.getBlobClient(fileName).openInputStream();
    }

    public String getUrl(String fileName) {
        return containerClient.getBlobClient(fileName).getBlobUrl();
    }
}
