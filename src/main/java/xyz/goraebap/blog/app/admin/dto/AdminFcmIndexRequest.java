package xyz.goraebap.blog.app.admin.dto;

import lombok.Data;

@Data
public class AdminFcmIndexRequest {
    private String search = "";
    private int page = 1;
    private int size = 20;
}
