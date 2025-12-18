package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import lombok.Data;

@Data
public class AdminSeriesIndexRequest {
    private String name = "";
    private String status = "";
    private String isPublishedYn = "";
    private int page = 1;
    private int size = 10;
}
