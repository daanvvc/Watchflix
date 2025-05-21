import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.file.*;

public class MakingEicarMP4 {
    public static void main(String[] args) throws IOException {
        // Load MP4 file from resources
        Resource resource = new ClassPathResource("valid-mp4-with-h264.mp4");

        // Create a temporary location to write the modified file
        Path modifiedFile = Paths.get("target/eicar-test-file.mp4");
        Files.createDirectories(modifiedFile.getParent());

        // Copy original MP4 from resource to target
        Files.copy(resource.getInputStream(), modifiedFile, StandardCopyOption.REPLACE_EXISTING);

        // EICAR test string (standard antivirus test string)
        String eicarString = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";

        // Append EICAR string to the MP4 file
        try (FileOutputStream fos = new FileOutputStream(modifiedFile.toFile(), true)) {
            fos.write(eicarString.getBytes());
        }

        System.out.println("EICAR string appended to MP4 file at: " + modifiedFile.toAbsolutePath());
    }
}
