package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminPostViewModel implements ThumbnailEnrichable {
    private Long id;
    private String slug;
    private String title;
    private String summary;
    private String content;
    private String postType;
    private String tags;
    private String isPublishedYn;
    private int viewCount;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;

    public boolean isPublished() {
        return "Y".equals(isPublishedYn);
    }
}
