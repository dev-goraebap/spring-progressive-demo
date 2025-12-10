package xyz.goraebap.spring_progressive_demo.app.post.dto;

import lombok.Data;

@Data
public class AdminPostIndexRequest {
    private String title = "";
    private String postType = "";
    private String isPublishedYn = "";
    private int page = 1;
    private int size = 10;
}
