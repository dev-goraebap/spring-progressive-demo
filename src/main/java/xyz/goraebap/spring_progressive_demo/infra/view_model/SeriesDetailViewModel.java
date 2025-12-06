package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SeriesDetailViewModel implements ThumbnailEnrichable {
    private Long id;
    private String slug;
    private String name;
    private String status;
    private LocalDateTime publishedAt;
    private String thumbnailKey;
    private String thumbnailMetadata;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;
    private List<PostItem> posts = new ArrayList<>();

    @Data
    public static class PostItem implements ThumbnailEnrichable {
        private Long id;
        private String slug;
        private String title;
        private String summary;
        private int viewCount;
        private int commentCount;
        private String thumbnailKey;
        private String thumbnailMetadata;

        // After Binding
        private String thumbnailUrl;
        private String thumbnailDominantColor;
    }
}
