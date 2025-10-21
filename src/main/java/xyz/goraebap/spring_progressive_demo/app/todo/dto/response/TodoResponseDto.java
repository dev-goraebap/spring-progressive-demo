package xyz.goraebap.spring_progressive_demo.app.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "할 일 응답 DTO")
@Builder
public record TodoResponseDto(
    @Schema(description = "할 일 ID", example = "1")
    Long id,

    @Schema(description = "할 일 제목", example = "Spring Boot 공부하기")
    String title,

    @Schema(description = "할 일 내용", example = "Swagger와 Scalar를 사용한 API 문서화 방법 익히기")
    String content,

    @Schema(description = "할 일 완료여부", type = "boolean", example = "false")
    boolean isCompleted,

    @Schema(description = "생성 일시", example = "2025-10-20T14:30:00")
    LocalDateTime createdAt
) {}
