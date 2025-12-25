package xyz.goraebap.blog.app.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "피드 목록 요청")
public class FeedIndexRequest {

    @Schema(description = "정렬 (예: viewCount,desc)")
    private String sort = "viewCount,desc";

    @Schema(description = "페이지 번호 (1부터 시작)")
    private int page = 1;
}
