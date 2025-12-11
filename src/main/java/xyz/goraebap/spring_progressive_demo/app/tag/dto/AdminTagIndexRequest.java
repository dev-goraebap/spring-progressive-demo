package xyz.goraebap.spring_progressive_demo.app.tag.dto;

import lombok.Data;

@Data
public class AdminTagIndexRequest {
    private String name = "";
    private int page = 1;
    private int size = 10;
}
