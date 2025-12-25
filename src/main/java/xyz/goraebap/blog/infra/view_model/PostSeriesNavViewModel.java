package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

/**
 * 포스트 상세에서 시리즈 네비게이션 정보
 */
@Data
public class PostSeriesNavViewModel implements ThumbnailEnrichable {
    // 시리즈 정보
    private Long seriesId;
    private String seriesSlug;
    private String seriesName;
    private String seriesStatus;
    private String thumbnailKey;
    private String thumbnailMetadata;
    private int totalCount;
    private int currentOrder;

    // After Binding
    private String thumbnailUrl;
    private String thumbnailDominantColor;

    // 이전/다음 포스트
    private NavItem prev;
    private NavItem next;

    @Data
    public static class NavItem {
        private Long id;
        private String slug;
        private String title;
    }
}
