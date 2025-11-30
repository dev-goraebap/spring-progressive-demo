package xyz.goraebap.spring_progressive_demo.app.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "패치노트 목록 요청")
public class PatchNoteIndexRequest {

    @Schema(description = "정렬 (예: publishedAt,desc)")
    private String sort = "publishedAt,desc";

    @Schema(description = "페이지 번호 (1부터 시작)")
    private int page = 1;
}
