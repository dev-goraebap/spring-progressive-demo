package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeriesPostViewModel implements ThumbnailEnrichable {
    private Long id;           // series_posts.id
    private Long postId;
    private String postTitle;
    private String postSlug;
    private int sortOrder;
    private LocalDateTime createdAt;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
}
