package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.Table;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.AdminPostViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;
import xyz.goraebap.blog.infra.view_model.PostViewModel;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jooq.Tables.*;
import static org.jooq.impl.DSL.*;

/**
 * 게시물 조회 서비스
 * - 클라이언트/관리자 페이지용 게시물 목록/상세 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final DSLContext dsl;
    private final ThumbnailEnricher thumbnailEnricher;

    /**
     * 클라이언트용 게시물 목록 조회 (페이지네이션)
     * - postType 기본값 "post"
     *
     * @param page 페이지 번호
     * @param sort 정렬 기준 (예: "publishedAt,DESC")
     * @return 페이지네이션된 게시물 목록
     */
    public Pagination<PostViewModel> getPostsWithPagination(int page, String sort) {
        return getPostsWithPagination(page, sort, "post");
    }

    /**
     * 클라이언트용 게시물 목록 조회 (페이지네이션)
     * - 발행된 게시물만 조회
     * - game-series 시리즈에 포함된 게시물 제외
     * - 태그 정보 포함
     *
     * @param page 페이지 번호
     * @param sort 정렬 기준 (예: "viewCount,DESC", "publishedAt,ASC")
     * @param postType 게시물 타입 ("post", "patch-note" 등)
     * @return 페이지네이션된 게시물 목록
     */
    public Pagination<PostViewModel> getPostsWithPagination(int page, String sort, String postType) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page);
        int size = Pagination.DEFAULT_PAGE_SIZE;

        // game-series 시리즈에 포함된 게시물 제외 조건
        var gameSeriesPostIds = select(SERIES_POSTS.POST_ID)
                .from(SERIES_POSTS)
                .innerJoin(SERIES).on(SERIES.ID.eq(SERIES_POSTS.SERIES_ID))
                .where(SERIES.SLUG.eq("game-series"));

        Condition condition = POSTS.POST_TYPE.eq(postType)
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .and(POSTS.ID.notIn(gameSeriesPostIds));

        // 전체 개수 조회
        int totalCount = dsl.fetchCount(POSTS, condition);

        // 정렬 필드 결정
        SortField<?> primarySort = getSortField(sortBy, sortDir);
        SortField<?> secondarySort = POSTS.PUBLISHED_AT.desc();

        var thumbnail = thumbnailSubquery("post");

        // 댓글 수 서브쿼리
        var commentCounts = select(COMMENTS.POST_ID, count().as("cnt"))
                .from(COMMENTS)
                .where(COMMENTS.DELETED_AT.isNull())
                .groupBy(COMMENTS.POST_ID)
                .asTable("comment_counts");

        // 게시물 목록 조회
        var posts = dsl.select(
                        POSTS.ID,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.VIEW_COUNT,
                        POSTS.PUBLISHED_AT,
                        coalesce(commentCounts.field("cnt", Integer.class), 0).as("comment_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(POSTS)
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(commentCounts).on(commentCounts.field("post_id", Long.class).eq(POSTS.ID))
                .where(condition)
                .orderBy(primarySort, secondarySort)
                .limit(size)
                .offset(offset)
                .fetchInto(PostViewModel.class);

        // 태그 조회 및 매핑
        enrichPostsWithTags(posts);
        posts.forEach(thumbnailEnricher::enrich);

        return Pagination.of(posts, page, totalCount, sort);
    }

    /**
     * slug로 게시물 상세 조회 (클라이언트용)
     * - postType 기본값 "post"
     *
     * @param slug 게시물 slug
     * @return 게시물 상세 (없으면 null)
     */
    public PostViewModel getPostBySlug(String slug) {
        return getPostBySlug(slug, "post");
    }

    /**
     * slug로 게시물 상세 조회 (클라이언트용)
     * - 발행된 게시물만 조회
     * - 태그, 댓글 수, 썸네일 정보 포함
     * - content 포함
     *
     * @param slug 게시물 slug
     * @param postType 게시물 타입
     * @return 게시물 상세 (없으면 null)
     */
    public PostViewModel getPostBySlug(String slug, String postType) {
        var thumbnail = thumbnailSubquery("post");

        // 댓글 수 서브쿼리
        var commentCounts = select(COMMENTS.POST_ID, count().as("cnt"))
                .from(COMMENTS)
                .where(COMMENTS.DELETED_AT.isNull())
                .groupBy(COMMENTS.POST_ID)
                .asTable("comment_counts");

        var post = dsl.select(
                        POSTS.ID,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.CONTENT,
                        POSTS.VIEW_COUNT,
                        POSTS.PUBLISHED_AT,
                        coalesce(commentCounts.field("cnt", Integer.class), 0).as("comment_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(POSTS)
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(commentCounts).on(commentCounts.field("post_id", Long.class).eq(POSTS.ID))
                .where(POSTS.SLUG.eq(slug))
                .and(POSTS.POST_TYPE.eq(postType))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .fetchOneInto(PostViewModel.class);

        if (post != null) {
            enrichPostWithTags(post);
            thumbnailEnricher.enrich(post);
        }

        return post;
    }

    /**
     * 최신 패치노트 조회
     *
     * @return 최신 패치노트 (없으면 null)
     */
    public PostViewModel getLatestPatchNote() {
        var thumbnail = thumbnailSubquery("post");

        // 댓글 수 서브쿼리
        var commentCounts = select(COMMENTS.POST_ID, count().as("cnt"))
                .from(COMMENTS)
                .where(COMMENTS.DELETED_AT.isNull())
                .groupBy(COMMENTS.POST_ID)
                .asTable("comment_counts");

        var post = dsl.select(
                        POSTS.ID,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.VIEW_COUNT,
                        POSTS.PUBLISHED_AT,
                        coalesce(commentCounts.field("cnt", Integer.class), 0).as("comment_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(POSTS)
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(commentCounts).on(commentCounts.field("post_id", Long.class).eq(POSTS.ID))
                .where(POSTS.POST_TYPE.eq("patch-note"))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(POSTS.PUBLISHED_AT.desc())
                .limit(1)
                .fetchOneInto(PostViewModel.class);

        if (post != null) {
            thumbnailEnricher.enrich(post);
        }

        return post;
    }

    /**
     * 최근 1주일 내 발행된 글이 있는지 확인
     *
     * @return 1주일 내 발행된 글 존재 여부
     */
    public boolean hasPublishedWithinWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        return dsl.fetchExists(
                selectOne()
                        .from(POSTS)
                        .where(POSTS.POST_TYPE.eq("post"))
                        .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                        .and(POSTS.PUBLISHED_AT.ge(oneWeekAgo))
        );
    }

    /**
     * 관리자용 게시물 목록 조회 (페이지네이션)
     * - postType, isPublishedYn, title 필터 지원
     * - 태그, 썸네일 정보 포함
     *
     * @param postType 게시물 타입 필터
     * @param isPublishedYn 발행 여부 필터
     * @param title 제목 검색어
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 게시물 목록
     */
    public Pagination<AdminPostViewModel> getAdminPostsWithPagination(
            String postType, String isPublishedYn, String title, int page, int size) {
        int offset = Pagination.getOffset(page, size);

        // 필터 조건 구성
        Condition condition = noCondition();
        if (postType != null && !postType.isEmpty()) {
            condition = condition.and(POSTS.POST_TYPE.eq(postType));
        }
        if (isPublishedYn != null && !isPublishedYn.isEmpty()) {
            condition = condition.and(POSTS.IS_PUBLISHED_YN.eq(isPublishedYn));
        }
        if (title != null && !title.isEmpty()) {
            condition = condition.and(POSTS.TITLE.likeIgnoreCase("%" + title + "%"));
        }

        // 전체 개수 조회
        int totalCount = dsl.fetchCount(POSTS, condition);

        var thumbnail = thumbnailSubquery("post");

        // 태그 집계 서브쿼리
        var tagsAgg = select(
                POST_TAGS.POST_ID,
                groupConcat(TAGS.NAME).separator(",").as("tags_agg")
        )
                .from(POST_TAGS)
                .innerJoin(TAGS).on(TAGS.ID.eq(POST_TAGS.TAG_ID))
                .groupBy(POST_TAGS.POST_ID)
                .asTable("tags_agg");

        // 게시물 목록 조회
        var posts = dsl.select(
                        POSTS.ID,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.POST_TYPE,
                        POSTS.IS_PUBLISHED_YN,
                        POSTS.VIEW_COUNT,
                        POSTS.PUBLISHED_AT,
                        POSTS.CREATED_AT,
                        tagsAgg.field("tags_agg", String.class).as("tags"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(POSTS)
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(tagsAgg).on(tagsAgg.field("post_id", Long.class).eq(POSTS.ID))
                .where(condition)
                .orderBy(POSTS.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(AdminPostViewModel.class);

        posts.forEach(thumbnailEnricher::enrich);

        return Pagination.of(posts, page, totalCount, null, size);
    }

    /**
     * ID로 게시물 상세 조회 (관리자용)
     * - content, 태그, 썸네일 정보 포함
     *
     * @param id 게시물 ID
     * @return 게시물 상세 (없으면 null)
     */
    public AdminPostViewModel getPostById(Long id) {
        var thumbnail = thumbnailSubquery("post");

        // 태그 집계 서브쿼리
        var tagsAgg = select(
                POST_TAGS.POST_ID,
                groupConcat(TAGS.NAME).separator(",").as("tags_agg")
        )
                .from(POST_TAGS)
                .innerJoin(TAGS).on(TAGS.ID.eq(POST_TAGS.TAG_ID))
                .where(POST_TAGS.POST_ID.eq(id))
                .groupBy(POST_TAGS.POST_ID)
                .asTable("tags_agg");

        var post = dsl.select(
                        POSTS.ID,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.CONTENT,
                        POSTS.POST_TYPE,
                        POSTS.IS_PUBLISHED_YN,
                        POSTS.VIEW_COUNT,
                        POSTS.PUBLISHED_AT,
                        POSTS.CREATED_AT,
                        tagsAgg.field("tags_agg", String.class).as("tags"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(POSTS)
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(tagsAgg).on(trueCondition())
                .where(POSTS.ID.eq(id))
                .fetchOneInto(AdminPostViewModel.class);

        if (post != null) {
            thumbnailEnricher.enrich(post);
        }

        return post;
    }

    // ========== Private Methods ==========

    /**
     * 썸네일 조회용 서브쿼리 생성
     *
     * @param recordType 레코드 타입 ("post")
     * @return 서브쿼리 테이블
     */
    private Table<?> thumbnailSubquery(String recordType) {
        return select(
                ATTACHMENTS.RECORD_ID,
                BLOBS.KEY.as("thumbnail_key"),
                BLOBS.METADATA.as("thumbnail_metadata")
        )
                .from(ATTACHMENTS)
                .leftJoin(BLOBS).on(BLOBS.ID.eq(ATTACHMENTS.BLOB_ID))
                .where(ATTACHMENTS.RECORD_TYPE.eq(recordType))
                .and(ATTACHMENTS.NAME.eq("thumbnail"))
                .asTable("thumbnail_" + recordType);
    }

    /**
     * 정렬 기준에 따른 SortField 반환
     *
     * @param sortBy 정렬 기준 필드명
     * @param sortDir 정렬 방향 ("ASC" 또는 "DESC")
     * @return JOOQ SortField
     */
    private SortField<?> getSortField(String sortBy, String sortDir) {
        boolean isAsc = "ASC".equalsIgnoreCase(sortDir);

        return switch (sortBy) {
            case "viewCount" -> isAsc ? POSTS.VIEW_COUNT.asc() : POSTS.VIEW_COUNT.desc();
            case "publishedAt" -> isAsc ? POSTS.PUBLISHED_AT.asc() : POSTS.PUBLISHED_AT.desc();
            default -> POSTS.PUBLISHED_AT.desc();
        };
    }

    /**
     * 게시물 목록에 태그 정보 추가
     * - 별도 쿼리로 태그 조회 후 매핑
     *
     * @param posts 게시물 목록
     */
    private void enrichPostsWithTags(List<PostViewModel> posts) {
        if (posts.isEmpty()) {
            return;
        }

        List<Long> postIds = posts.stream()
                .map(PostViewModel::getId)
                .toList();

        // 태그 조회
        var tagRecords = dsl.select(POST_TAGS.POST_ID, TAGS.NAME)
                .from(POST_TAGS)
                .innerJoin(TAGS).on(TAGS.ID.eq(POST_TAGS.TAG_ID))
                .where(POST_TAGS.POST_ID.in(postIds))
                .fetch();

        // postId -> tags 맵 생성
        Map<Long, List<String>> tagsMap = tagRecords.stream()
                .collect(Collectors.groupingBy(
                        r -> r.get(POST_TAGS.POST_ID),
                        Collectors.mapping(r -> r.get(TAGS.NAME), Collectors.toList())
                ));

        // 각 게시물에 태그 설정
        posts.forEach(post -> post.setTags(tagsMap.getOrDefault(post.getId(), Collections.emptyList())));
    }

    /**
     * 단일 게시물에 태그 정보 추가
     *
     * @param post 게시물
     */
    private void enrichPostWithTags(PostViewModel post) {
        var tags = dsl.select(TAGS.NAME)
                .from(POST_TAGS)
                .innerJoin(TAGS).on(TAGS.ID.eq(POST_TAGS.TAG_ID))
                .where(POST_TAGS.POST_ID.eq(post.getId()))
                .fetch(TAGS.NAME);

        post.setTags(tags);
    }
}
