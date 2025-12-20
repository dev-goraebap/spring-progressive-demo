package xyz.goraebap.spring_progressive_demo.shared.r2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import xyz.goraebap.spring_progressive_demo.shared.config.R2Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class R2StorageService {

    private final S3Client s3Client;
    private final R2Properties r2Properties;

    public void uploadFile(String key, byte[] data, String contentType) {
        String filePath = r2Properties.getFilePath(key);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(r2Properties.bucketName())
                .key(filePath)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));
        log.info("File uploaded to R2: {}", filePath);
    }

    public void deleteFile(String key) {
        String filePath = r2Properties.getFilePath(key);
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(r2Properties.bucketName())
                .key(filePath)
                .build();

        s3Client.deleteObject(request);
        log.info("File deleted from R2: {}", filePath);
    }

    public String getPublicUrl(String key) {
        return r2Properties.getPublicUrl(key);
    }
}
