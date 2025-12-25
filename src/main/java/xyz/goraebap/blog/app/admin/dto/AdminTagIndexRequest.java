package xyz.goraebap.blog.app.admin.dto;

import lombok.Data;

@Data
public class AdminTagIndexRequest {
    private String name = "";
    private int page = 1;
    private int size = 10;
}
