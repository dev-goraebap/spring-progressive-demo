package xyz.goraebap.spring_progressive_demo.app.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "메인페이지 요청")
public class MainIndexRequest {
    private static final int PAGE_SIZE = 5;

    @Schema(description = "정렬타입")
    private String orderType;

    @Schema(description = "페이지 번호 (0부터 시작)")
    private int page = 0;

    public int getLimit() {
        return PAGE_SIZE;
    }

    public int getOffset() {
        return page * PAGE_SIZE;
    }

    public int getNextPage() {
        return page + 1;
    }
}
