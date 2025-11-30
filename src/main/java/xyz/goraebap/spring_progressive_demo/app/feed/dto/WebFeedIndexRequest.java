package xyz.goraebap.spring_progressive_demo.app.feed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "메인페이지 요청")
public class WebFeedIndexRequest {

    @Schema(description = "정렬타입")
    private String orderType = "viewCount";

    @Schema(description = "페이지 번호 (0부터 시작)")
    private int page = 0;
}
