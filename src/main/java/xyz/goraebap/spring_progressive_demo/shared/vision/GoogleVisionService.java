package xyz.goraebap.spring_progressive_demo.shared.vision;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GoogleVisionService {

    @Value("${google.vision.credentials-path:}")
    private String credentialsPath;

    private ImageAnnotatorClient client;

    @PostConstruct
    public void init() {
        try {
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                var settings = ImageAnnotatorSettings.newBuilder()
                        .setCredentialsProvider(() ->
                            com.google.auth.oauth2.GoogleCredentials.fromStream(
                                new FileInputStream(credentialsPath)
                            )
                        )
                        .build();
                client = ImageAnnotatorClient.create(settings);
                log.info("Google Vision API 클라이언트 초기화 완료 (credentials: {})", credentialsPath);
            } else {
                log.warn("Google Vision credentials path가 설정되지 않았습니다. 색상 추출이 비활성화됩니다.");
            }
        } catch (IOException e) {
            log.error("Google Vision API 초기화 실패", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * 이미지에서 지배적인 색상을 추출합니다.
     * @param imageData 이미지 바이트 배열
     * @return 색상 정보 리스트 (hex, score 포함)
     */
    public List<ColorInfo> extractColors(byte[] imageData) {
        if (client == null) {
            log.debug("Google Vision 클라이언트가 초기화되지 않아 색상 추출을 건너뜁니다.");
            return Collections.emptyList();
        }

        try {
            ByteString imgBytes = ByteString.copyFrom(imageData);
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feature = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
            AnnotateImageResponse res = response.getResponses(0);

            if (res.hasError()) {
                log.error("Google Vision API 오류: {}", res.getError().getMessage());
                return Collections.emptyList();
            }

            var colors = res.getImagePropertiesAnnotation().getDominantColors().getColorsList();
            return colors.stream()
                    .map(colorInfo -> {
                        var color = colorInfo.getColor();
                        int red = (int) color.getRed();
                        int green = (int) color.getGreen();
                        int blue = (int) color.getBlue();
                        String hex = String.format("#%02x%02x%02x", red, green, blue);
                        return new ColorInfo(hex, colorInfo.getScore(), colorInfo.getPixelFraction());
                    })
                    .toList();

        } catch (Exception e) {
            log.error("색상 추출 중 오류 발생", e);
            return Collections.emptyList();
        }
    }

    public record ColorInfo(String hex, float score, float pixelFraction) {}
}
