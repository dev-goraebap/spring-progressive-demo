package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import lombok.Data;

@Data
public class AdminCommentIndexRequest {
    private String search = "";
    private int page = 1;
    private int size = 20;
}
