package xyz.goraebap.spring_progressive_demo.app.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "메인페이지 요청")
public class MainIndexRequest {
    @Schema(description = "정렬타입")
    private String orderType;
}
