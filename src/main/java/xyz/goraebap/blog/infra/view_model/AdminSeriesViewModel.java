package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminSeriesViewModel implements ThumbnailEnrichable {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String status;
    private String isPublishedYn;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private int postCount;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;

    public boolean isPublished() {
        return "Y".equals(isPublishedYn);
    }
}
