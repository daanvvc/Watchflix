package fontys.IA.services;

import fontys.IA.domain.MovieFile;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class SafeUploadService {
    @Value("${virustotal.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<String> ALLOWED_MIME_TYPES = List.of("video/mp4","video/quicktime", "video/x-m4v" );

    public boolean isValidMp4WithH264(Resource videoFile) throws IOException {
        byte[] bytes = StreamUtils.copyToByteArray(videoFile.getInputStream());

        Tika tika = new Tika();
        String detectedMimeType = tika.detect(bytes);

        // First check MIME type with Tika (doesn't rely on Content-Type header)
        if (!ALLOWED_MIME_TYPES.contains(detectedMimeType)) {
            return false;
        }

        // Then check for H.264 codec using FFmpeg
        InputStream codecCheckStream = new ByteArrayInputStream(bytes);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(codecCheckStream);

        grabber.start();
        String codecName = grabber.getVideoCodecName();
        grabber.stop();

        // H.264 can be identified as "h264" or "avc1"
        return codecName != null && (codecName.equalsIgnoreCase("h264") ||
                codecName.toLowerCase().contains("avc"));
    }

    public boolean hasValidFileName(String fileName) {
        return fileName.length() <= 256;
    }

    public boolean isMaliciousVirusTotalScan(MovieFile movieFile) throws IOException {
        byte[] bytes = StreamUtils.copyToByteArray(movieFile.getFile().getInputStream());
        // if the size is larger than 32 MB, this will not work
        if (bytes.length > 32 * 1024 * 1024) {
            return false;
        }

        // Set up the http request
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apikey", apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // With the file with a name
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return movieFile.getFileName();
            }
        });

        // Post the request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://www.virustotal.com/api/v3/files",
                requestEntity,
                String.class);

        // Parse the response to get the analysis ID
        JsonNode root = objectMapper.readTree(response.getBody());
        String analysisId = root.path("data").path("id").asText();

        // Wait and check the analysis results
        return checkAnalysisResult(analysisId);
    }

    private boolean checkAnalysisResult(String analysisId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apikey", apiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Get results
        for (int i = 0; i < 10; i++) {
            ResponseEntity<String> resultResponse = restTemplate.exchange(
                    "https://www.virustotal.com/api/v3/analyses/" + analysisId,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            // Get the status of the result
            JsonNode resultRoot = objectMapper.readTree(resultResponse.getBody());
            String status = resultRoot.path("data").path("attributes").path("status").asText();

            if ("completed".equals(status)) {
                // Check if any malicious results were found
                JsonNode stats = resultRoot.path("data").path("attributes").path("stats");
                int malicious = stats.path("malicious").asInt();
                int suspicious = stats.path("suspicious").asInt();

                return malicious == 0 && suspicious == 0;
            }

            try {
                Thread.sleep(5000); // Wait 5 seconds between checks
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for scan results", e);
            }
        }

        // If we couldn't get a result after multiple attempts
        throw new IOException("Could not get scan results in time");
    }
}
