package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SeriesDetailViewModel implements ThumbnailEnrichable {
    private Long id;
    private String slug;
    private String name;
    private String description;
    private String status;
    private LocalDateTime publishedAt;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
    private List<PostViewModel> posts = new ArrayList<>();
}
