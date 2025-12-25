package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeriesViewModel implements ThumbnailEnrichable {
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
