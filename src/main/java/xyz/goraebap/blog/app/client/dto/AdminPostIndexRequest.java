package xyz.goraebap.blog.app.client.dto;

import lombok.Data;

@Data
public class AdminPostIndexRequest {
    private String title = "";
    private String postType = "";
    private String isPublishedYn = "";
    private int page = 1;
    private int size = 10;
}
