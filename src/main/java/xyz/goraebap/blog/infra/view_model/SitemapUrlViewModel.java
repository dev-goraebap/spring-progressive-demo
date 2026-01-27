package xyz.goraebap.blog.infra.view_model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Sitemap URL 정보
 * - sitemap.xml 생성용 뷰모델
 */
@Data
@AllArgsConstructor
public class SitemapUrlViewModel {

    /** 페이지 URL 경로 (예: /posts/my-post) */
    private String loc;

    /** 마지막 수정일시 */
    private LocalDateTime lastmod;

    /** 변경 빈도 (daily, weekly, monthly 등) */
    private String changefreq;

    /** 우선순위 (0.0 ~ 1.0) */
    private String priority;
}
