package xyz.goraebap.spring_progressive_demo.app.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "할 일 검색 DTO")
public record TodoSearchDto(
    @Schema(
        description = "검색할 제목 (부분 일치)",
        example = "회의"
    )
    String title,

    @Schema(
        description = "완료 여부",
        example = "false"
    )
    Boolean isCompleted,

    @Schema(
        description = "커서 기준 생성일자 (해당 시간 이전 데이터 조회)",
        example = "2025-01-01T00:00:00"
    )
    LocalDateTime cursor,

    @Schema(
        description = "정렬 순서 (ASC: 오름차순, DESC: 내림차순)",
        example = "DESC",
        defaultValue = "DESC"
    )
    @Pattern(regexp = "ASC|DESC", message = "정렬 순서는 ASC 또는 DESC만 가능합니다")
    String sortOrder,

    @Schema(
        description = "페이지당 조회 개수",
        example = "20",
        defaultValue = "20"
    )
    @Min(value = 1, message = "조회 개수는 최소 1개 이상이어야 합니다")
    @Max(value = 100, message = "조회 개수는 최대 100개까지 가능합니다")
    Integer perPage
) {
}
