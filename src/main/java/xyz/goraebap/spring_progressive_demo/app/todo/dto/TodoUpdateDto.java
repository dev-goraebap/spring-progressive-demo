package xyz.goraebap.spring_progressive_demo.app.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "할 일 생성 DTO")
public record TodoUpdateDto(
    @Schema(
        description = "할 일 제목",
        example = "Spring Boot 공부하기",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
    String title,

    @Schema(
        description = "할 일 내용",
        example = "Swagger와 Scalar를 사용한 API 문서화 방법 익히기"
    )
    @Size(max = 500, message = "내용은 500자 이하여야 합니다")
    String content
) {}
