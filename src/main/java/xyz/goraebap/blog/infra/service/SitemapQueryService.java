package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.SitemapUrlViewModel;
import xyz.goraebap.blog.shared.config.CacheConfig;

import java.util.ArrayList;
import java.util.List;

import static jooq.Tables.*;

/**
 * Sitemap 조회 서비스
 * - sitemap.xml 생성을 위한 URL 목록 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class SitemapQueryService {

    private final DSLContext dsl;

    /**
     * sitemap.xml용 전체 URL 목록 조회
     * - 정적 페이지 + 동적 콘텐츠 URL
     * - 발행된 게시물, 시리즈만 포함
     *
     * @return sitemap URL 목록
     */
    @Cacheable(value = CacheConfig.SITEMAP, key = "'all'")
    public List<SitemapUrlViewModel> getAllUrls() {
        List<SitemapUrlViewModel> urls = new ArrayList<>();

        // 1. 정적 페이지
        urls.add(new SitemapUrlViewModel("/", null, "daily", "1.0"));
        urls.add(new SitemapUrlViewModel("/series", null, "weekly", "0.8"));
        urls.add(new SitemapUrlViewModel("/curations", null, "daily", "0.7"));
        urls.add(new SitemapUrlViewModel("/patch-notes", null, "weekly", "0.5"));

        // 2. 게시물 (post 타입만, game-series 제외)
        var gameSeriesPostIds = dsl.select(SERIES_POSTS.POST_ID)
                .from(SERIES_POSTS)
                .innerJoin(SERIES).on(SERIES.ID.eq(SERIES_POSTS.SERIES_ID))
                .where(SERIES.SLUG.eq("game-series"));

        var posts = dsl.select(POSTS.SLUG, POSTS.PUBLISHED_AT)
                .from(POSTS)
                .where(POSTS.POST_TYPE.eq("post"))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .and(POSTS.ID.notIn(gameSeriesPostIds))
                .orderBy(POSTS.PUBLISHED_AT.desc())
                .fetch();

        posts.forEach(record -> urls.add(new SitemapUrlViewModel(
                "/posts/" + record.get(POSTS.SLUG),
                record.get(POSTS.PUBLISHED_AT),
                "monthly",
                "0.8"
        )));

        // 3. 패치노트
        var patchNotes = dsl.select(POSTS.SLUG, POSTS.PUBLISHED_AT)
                .from(POSTS)
                .where(POSTS.POST_TYPE.eq("patch-note"))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(POSTS.PUBLISHED_AT.desc())
                .fetch();

        patchNotes.forEach(record -> urls.add(new SitemapUrlViewModel(
                "/patch-notes/" + record.get(POSTS.SLUG),
                record.get(POSTS.PUBLISHED_AT),
                "monthly",
                "0.5"
        )));

        // 4. 시리즈
        var seriesList = dsl.select(SERIES.SLUG, SERIES.PUBLISHED_AT)
                .from(SERIES)
                .where(SERIES.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(SERIES.PUBLISHED_AT.desc())
                .fetch();

        seriesList.forEach(record -> urls.add(new SitemapUrlViewModel(
                "/series/" + record.get(SERIES.SLUG),
                record.get(SERIES.PUBLISHED_AT),
                "weekly",
                "0.7"
        )));

        return urls;
    }
}
