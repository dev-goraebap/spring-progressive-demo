package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeriesViewModel {
    private Long id;
    private String slug;
    private String name;
    private String description;
    private String status;
    private LocalDateTime publishedAt;
    private int postCount;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
}
