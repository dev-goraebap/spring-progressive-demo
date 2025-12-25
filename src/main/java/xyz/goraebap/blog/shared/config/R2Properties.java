package xyz.goraebap.blog.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "r2")
public record R2Properties(
    String bucketName,
    String endpoint,
    String accessKeyId,
    String secretAccessKey,
    String publicUrl
) {
    /**
     * key를 디렉토리 구조가 포함된 파일 경로로 변환
     * 예: "abcd1234.jpg" -> "ab/cd/abcd1234.jpg"
     */
    public String getFilePath(String key) {
        return key.substring(0, 2) + "/" + key.substring(2, 4) + "/" + key;
    }

    /**
     * key로 공개 URL 생성
     * 예: "https://cdn.example.com/bucket-name/ab/cd/abcd1234.jpg"
     */
    public String getPublicUrl(String key) {
        String filePath = getFilePath(key);
        return publicUrl + "/" + bucketName + "/" + filePath;
    }
}
