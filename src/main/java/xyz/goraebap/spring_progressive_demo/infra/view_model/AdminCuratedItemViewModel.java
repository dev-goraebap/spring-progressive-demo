package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCuratedItemViewModel {
    private Long id;
    private String title;
    private String link;
    private String snippet;
    private LocalDateTime pubDate;
    private String source;
    private Long sourceId;
    private LocalDateTime createdAt;
}
