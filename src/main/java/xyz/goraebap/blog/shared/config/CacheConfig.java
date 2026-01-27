package xyz.goraebap.blog.shared.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 캐시 설정
 * - Caffeine 기반 로컬 인메모리 캐시 구성
 * - 도메인별 캐시 이름 및 TTL 정의
 * - 클라이언트용 조회 메서드에만 캐싱 적용 (관리자 페이지 제외)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // ===== 캐시 이름 상수 =====

    /** 게시물 목록 캐시 */
    public static final String POSTS = "posts";

    /** 게시물 상세 캐시 */
    public static final String POST_DETAIL = "postDetail";

    /** 최신 패치노트 캐시 */
    public static final String LATEST_PATCH_NOTE = "latestPatchNote";

    /** 1주일 내 발행 여부 캐시 */
    public static final String PUBLISHED_WITHIN_WEEK = "publishedWithinWeek";

    /** 시리즈 목록 캐시 */
    public static final String SERIES = "series";

    /** 시리즈 상세 캐시 */
    public static final String SERIES_DETAIL = "seriesDetail";

    /** 시리즈 네비게이션 캐시 */
    public static final String SERIES_NAV = "seriesNav";

    /** 큐레이션 목록 캐시 */
    public static final String CURATIONS = "curations";

    /** 최신 큐레이션 캐시 */
    public static final String LATEST_CURATIONS = "latestCurations";

    /** 댓글 목록 캐시 */
    public static final String COMMENTS = "comments";

    /** Sitemap 캐시 */
    public static final String SITEMAP = "sitemap";

    /**
     * 캐시 매니저 빈 등록
     * - 각 캐시별 개별 TTL 및 최대 크기 설정
     * @return SimpleCacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        cacheManager.setCaches(List.of(
                // Posts - 12시간 (720분)
                buildCache(POSTS, 720, 200),
                buildCache(POST_DETAIL, 720, 100),
                buildCache(LATEST_PATCH_NOTE, 720, 1),
                buildCache(PUBLISHED_WITHIN_WEEK, 720, 1),

                // Series - 12시간 (720분)
                buildCache(SERIES, 720, 50),
                buildCache(SERIES_DETAIL, 720, 50),
                buildCache(SERIES_NAV, 720, 100),

                // Curations - 2시간 (120분)
                buildCache(CURATIONS, 120, 100),
                buildCache(LATEST_CURATIONS, 120, 10),

                // Comments - 12시간 (720분)
                buildCache(COMMENTS, 720, 200),

                // Sitemap - 12시간 (720분)
                buildCache(SITEMAP, 720, 1)
        ));

        return cacheManager;
    }

    /**
     * Caffeine 캐시 생성 헬퍼
     * @param name 캐시 이름
     * @param ttlMinutes TTL (분)
     * @param maxSize 최대 항목 수
     * @return CaffeineCache 인스턴스
     */
    private CaffeineCache buildCache(String name, long ttlMinutes, long maxSize) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .recordStats()
                        .build());
    }
}
