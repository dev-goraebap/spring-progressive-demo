package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewModel {
    private String title;
    private String summary;
    private int viewCount;
    private int commentCount;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;
}
