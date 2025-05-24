package fontys.IA.services;

import fontys.IA.domain.MovieFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SafeUploadService.class)
@ActiveProfiles("test")
class SafeUploadServiceSecurityTest {
    @Autowired
    private SafeUploadService safeUploadService;

    @Test
    // Succeeds a valid MP4 file with H.264 codec (file from https://sample-videos.com/)
    void ValidMp4WithH264_SuccesTest() throws IOException{
        // Arrange
        Resource videoFile = new ClassPathResource("valid-mp4-with-h264.mp4");

        // Act
        boolean result = safeUploadService.isValidMp4WithH264(videoFile);

        // Assert
        assertTrue(result);
    }

    @Test
    // Fails an unvalid non-MP4 file
    void InvalidFileTypeTxt_FailureTest() throws IOException {
        // Arrange
        Resource nonvideoFile = new ClassPathResource("not-valid-mp4.txt");

        // Act
        boolean result = safeUploadService.isValidMp4WithH264(nonvideoFile);

        // Assert
        assertFalse(result);
    }

    @Test
    // Fails a MP4 file with the different codec VP9 (https://toolsfairy.com/video-test/sample-webm-files#)
    void Mp4WithWrongCodecVP9_FailureTest() throws IOException {
        // Arrange
        Resource nonvideoFile = new ClassPathResource("mp4-vp9.mp4");

        // Act
        boolean result = safeUploadService.isValidMp4WithH264(nonvideoFile);

        // Assert
        assertFalse(result);
    }

    @Test
    // Fails a Resource file with an empty byte array
    void ResourceEmptyByteArray_FailureTest() throws IOException {
        // Arrange
        byte[] emptyBytes = new byte[0];
        Resource emptyResource = new ByteArrayResource(emptyBytes);

        // Act
        boolean result = safeUploadService.isValidMp4WithH264(emptyResource);

        // Assert
        assertFalse(result);
    }

    @Test
    // Fails a Resource file with a corrupted byte array (corrupted using https://pinetools.com/corrupt-a-file)
    void MP4Corrupted_FailureTest() throws IOException {
        // Arrange
        Resource corruptedVideoFile = new ClassPathResource("corrupted.mp4");

        // Act
        boolean result = safeUploadService.isValidMp4WithH264(corruptedVideoFile);

        // Assert
        assertFalse(result);
    }

    @Test
    // Succeeds a string with less than 256 characters
    void FileNameCorrectLength_SuccessTest() {
        // Arrange
        String correctFileName = "Correct";

        // Act
        boolean result = safeUploadService.hasValidFileName(correctFileName);

        // Assert
        assertTrue(result);
    }

    @Test
    // Fails a string with more than 256 characters
    void FileNameIncorrectLength_FailureTest() {
        // Arrange
        String incorrectFileName = "i".repeat(257);

        // Act
        boolean result = safeUploadService.hasValidFileName(incorrectFileName);

        // Assert
        assertFalse(result);
    }

    @Test
    // Succeeds a valid MP4 file in the VirusTotal scan (file from https://sample-videos.com/)
    void ValidMp4VirusTotalScan_SuccessTest() throws IOException {
        // Arrange
        Resource videoFile = new ClassPathResource("valid-mp4-with-h264.mp4");
        MovieFile movieFile = new MovieFile(UUID.randomUUID(), videoFile, "Success_Test_File");

        // Act
        boolean result = safeUploadService.isMaliciousVirusTotalScan(movieFile);

        // Assert
        assertTrue(result);
    }

    @Test
    void EICARTestFileVirusTotalScan_FailureTest() throws IOException {
        // Arrange
        Resource videoFile = new ClassPathResource("eicar-test-file.mp4");
        MovieFile movieFile = new MovieFile(UUID.randomUUID(), videoFile, "Failure_Test_File");

        // Act
        boolean validMp4Result = safeUploadService.isValidMp4WithH264(videoFile);
        boolean scanResult = safeUploadService.isMaliciousVirusTotalScan(movieFile);

        // Assert
        assertTrue(validMp4Result);
        assertFalse(scanResult);
    }
}