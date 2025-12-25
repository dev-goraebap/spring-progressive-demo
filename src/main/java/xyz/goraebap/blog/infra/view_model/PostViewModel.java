package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostViewModel implements ThumbnailEnrichable {
    private Long id;
    private String slug;
    private String title;
    private String summary;
    private int viewCount;
    private int commentCount;
    private String thumbnailKey;
    private String thumbnailMetadata;
    private LocalDateTime publishedAt;
    private List<String> tags;
    private String content;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
}
