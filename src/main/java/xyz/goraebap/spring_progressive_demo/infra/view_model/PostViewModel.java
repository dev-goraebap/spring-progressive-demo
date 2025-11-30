package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewModel {
    private Long id;
    private String title;
    private String summary;
    private int viewCount;
    private int commentCount;
    private String thumbnailKey;
    private String thumbnailMetadata;
    private LocalDateTime publishedAt;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
}
