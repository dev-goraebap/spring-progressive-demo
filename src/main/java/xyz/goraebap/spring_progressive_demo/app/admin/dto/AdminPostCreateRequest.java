package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AdminPostCreateRequest {
    private String slug;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String postType;

    private String isPublishedYn;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishedAt;

    private Long thumbnailBlobId;
}
