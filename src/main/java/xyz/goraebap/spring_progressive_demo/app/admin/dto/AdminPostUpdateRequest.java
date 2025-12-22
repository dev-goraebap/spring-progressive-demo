package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AdminPostUpdateRequest {
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "슬러그는 영문자, 숫자, 하이픈(-)만 사용할 수 있습니다")
    private String slug;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String postType;

    private String isPublishedYn;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishedAt;

    private Long thumbnailBlobId;
}
