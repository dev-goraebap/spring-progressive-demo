package xyz.goraebap.spring_progressive_demo.app.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.BlobEntity;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.BlobRepository;
import xyz.goraebap.spring_progressive_demo.shared.exception.BadRequestException;
import xyz.goraebap.spring_progressive_demo.shared.r2.R2StorageService;
import xyz.goraebap.spring_progressive_demo.shared.vision.GoogleVisionService;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MediaService {

    private final BlobRepository blobRepository;
    private final R2StorageService r2StorageService;
    private final GoogleVisionService googleVisionService;
    private final ObjectMapper objectMapper;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    public MediaUploadResponse uploadFile(MultipartFile file, Long userId) {
        validateFile(file);

        try {
            byte[] data = file.getBytes();
            String checksum = calculateChecksum(data);

            // 중복 파일 확인
            var existingBlob = blobRepository.findByChecksum(checksum);
            if (existingBlob.isPresent()) {
                log.info("기존 파일 재사용: {} (checksum: {})", existingBlob.get().getFilename(), checksum);
                return toResponse(existingBlob.get());
            }

            // R2 업로드
            String key = generateKey();
            r2StorageService.uploadFile(key, data, file.getContentType());

            // 메타데이터 추출 (이미지인 경우 색상 추출)
            String metadata = extractMetadata(data, file.getContentType());

            // DB 저장
            var blob = BlobEntity.create(
                    key,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    (int) file.getSize(),
                    checksum,
                    userId.toString(),
                    metadata
            );
            blobRepository.save(blob);

            return toResponse(blob);

        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            throw new BadRequestException("파일 업로드에 실패했습니다.");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("파일이 없습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("지원하지 않는 파일 형식입니다.");
        }
    }

    private String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String calculateChecksum(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private String extractMetadata(byte[] data, String contentType) {
        var metadata = new HashMap<String, Object>();

        // 이미지인 경우 지배적 색상 추출
        if (contentType != null && contentType.startsWith("image/")) {
            metadata.put("type", "image");

            var colors = googleVisionService.extractColors(data);
            if (!colors.isEmpty()) {
                metadata.put("dominantColor", colors.get(0).hex());
                if (colors.size() > 1) {
                    metadata.put("dominantColor2", colors.get(1).hex());
                }
            }
        }

        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            log.warn("메타데이터 직렬화 실패", e);
            return "{}";
        }
    }

    private MediaUploadResponse toResponse(BlobEntity blob) {
        return new MediaUploadResponse(
                blob.getId(),
                r2StorageService.getPublicUrl(blob.getKey())
        );
    }

    public record MediaUploadResponse(Long blobId, String url) {}
}
