package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.*;
import xyz.goraebap.blog.shared.config.CacheConfig;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import java.util.List;

import static jooq.Tables.*;
import static org.jooq.impl.DSL.*;

/**
 * 시리즈 조회 서비스
 * - 클라이언트/관리자 페이지용 시리즈 목록/상세 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class SeriesQueryService {

    private final DSLContext dsl;
    private final ThumbnailEnricher thumbnailEnricher;

    /**
     * 발행된 시리즈 전체 목록 조회 (클라이언트용)
     * - 상태별 정렬: COMPLETED > PROGRESS > PLAN
     * - 각 시리즈별 발행된 포스트 수 포함
     * - 썸네일 정보 포함
     *
     * @return 발행된 시리즈 목록
     */
    @Cacheable(value = CacheConfig.SERIES, key = "'all'")
    public List<SeriesViewModel> getAllSeries() {
        // 시리즈별 발행된 포스트 수 서브쿼리
        var postCountSubquery = select(SERIES_POSTS.SERIES_ID, count().as("cnt"))
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .where(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .groupBy(SERIES_POSTS.SERIES_ID)
                .asTable("post_counts");

        var thumbnail = thumbnailSubquery("series");

        var seriesList = dsl.select(
                        SERIES.ID,
                        SERIES.SLUG,
                        SERIES.NAME,
                        SERIES.DESCRIPTION,
                        SERIES.STATUS,
                        SERIES.PUBLISHED_AT,
                        coalesce(postCountSubquery.field("cnt", Integer.class), 0).as("post_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES)
                .leftJoin(postCountSubquery).on(postCountSubquery.field("series_id", Long.class).eq(SERIES.ID))
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(SERIES.ID.cast(String.class)))
                .where(SERIES.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(
                        case_(SERIES.STATUS)
                                .when("COMPLETED", 1)
                                .when("PROGRESS", 2)
                                .when("PLAN", 3)
                                .else_(4),
                        SERIES.CREATED_AT.desc()
                )
                .fetchInto(SeriesViewModel.class);

        seriesList.forEach(thumbnailEnricher::enrich);
        return seriesList;
    }

    /**
     * slug로 시리즈 상세 조회 (클라이언트용)
     * - 시리즈 정보 + 포함된 발행 포스트 목록
     * - 포스트는 sort_order 순 정렬
     * - 각 포스트별 댓글 수 포함
     *
     * @param slug 시리즈 slug
     * @return 시리즈 상세 정보 (없으면 null)
     */
    @Cacheable(value = CacheConfig.SERIES_DETAIL, key = "#slug")
    public SeriesDetailViewModel getSeriesWithPosts(String slug) {
        var seriesThumbnail = thumbnailSubquery("series");

        // 시리즈 기본 정보 조회
        var seriesRecord = dsl.select(
                        SERIES.ID,
                        SERIES.SLUG,
                        SERIES.NAME,
                        SERIES.DESCRIPTION,
                        SERIES.STATUS,
                        SERIES.PUBLISHED_AT,
                        seriesThumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        seriesThumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES)
                .leftJoin(seriesThumbnail).on(seriesThumbnail.field("record_id", String.class).eq(SERIES.ID.cast(String.class)))
                .where(SERIES.SLUG.eq(slug))
                .and(SERIES.IS_PUBLISHED_YN.eq("Y"))
                .fetchOneInto(SeriesDetailViewModel.class);

        if (seriesRecord == null) {
            return null;
        }

        var postThumbnail = thumbnailSubquery("post");

        // 댓글 수 서브쿼리
        var commentCounts = select(COMMENTS.POST_ID, count().as("cnt"))
                .from(COMMENTS)
                .where(COMMENTS.DELETED_AT.isNull())
                .groupBy(COMMENTS.POST_ID)
                .asTable("comment_counts");

        // 시리즈에 포함된 발행 포스트 목록 조회
        var posts = dsl.select(
                        POSTS.ID,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        POSTS.SUMMARY,
                        POSTS.VIEW_COUNT,
                        coalesce(commentCounts.field("cnt", Integer.class), 0).as("comment_count"),
                        postThumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        postThumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .leftJoin(postThumbnail).on(postThumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .leftJoin(commentCounts).on(commentCounts.field("post_id", Long.class).eq(POSTS.ID))
                .where(SERIES_POSTS.SERIES_ID.eq(seriesRecord.getId()))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(SERIES_POSTS.SORT_ORDER.asc())
                .fetchInto(PostViewModel.class);

        thumbnailEnricher.enrich(seriesRecord);
        posts.forEach(thumbnailEnricher::enrich);
        seriesRecord.setPosts(posts);

        return seriesRecord;
    }

    /**
     * 포스트 상세에서 시리즈 네비게이션 정보 조회
     * - 현재 포스트가 속한 시리즈 정보
     * - 이전/다음 포스트 정보
     * - 전체 포스트 수 및 현재 순서
     *
     * @param postId 포스트 ID
     * @return 시리즈 네비게이션 정보 (시리즈에 속하지 않으면 null)
     */
    @Cacheable(value = CacheConfig.SERIES_NAV, key = "#postId")
    public PostSeriesNavViewModel getSeriesNavByPostId(Long postId) {
        // 현재 포스트가 속한 시리즈 정보 조회
        var seriesBase = dsl.select(
                        SERIES.ID.as("series_id"),
                        SERIES.SLUG.as("series_slug"),
                        SERIES.NAME.as("series_name"),
                        SERIES.STATUS.as("series_status"),
                        SERIES_POSTS.SORT_ORDER.as("raw_order")
                )
                .from(SERIES_POSTS)
                .innerJoin(SERIES).on(SERIES.ID.eq(SERIES_POSTS.SERIES_ID))
                .where(SERIES_POSTS.POST_ID.eq(postId))
                .and(SERIES.IS_PUBLISHED_YN.eq("Y"))
                .fetchOne();

        if (seriesBase == null) {
            return null;
        }

        Long seriesId = seriesBase.get("series_id", Long.class);
        Integer rawOrder = seriesBase.get("raw_order", Integer.class);

        // 시리즈 썸네일 조회
        var thumbnailRecord = dsl.select(BLOBS.KEY, BLOBS.METADATA)
                .from(ATTACHMENTS)
                .innerJoin(BLOBS).on(BLOBS.ID.eq(ATTACHMENTS.BLOB_ID))
                .where(ATTACHMENTS.RECORD_TYPE.eq("series"))
                .and(ATTACHMENTS.NAME.eq("thumbnail"))
                .and(ATTACHMENTS.RECORD_ID.eq(seriesId.toString()))
                .fetchOne();

        // 시리즈 내 발행된 포스트들 (정렬 순서대로)
        var seriesPostsOrdered = dsl.select(
                        SERIES_POSTS.POST_ID,
                        SERIES_POSTS.SORT_ORDER,
                        POSTS.SLUG,
                        POSTS.TITLE,
                        rowNumber().over(orderBy(SERIES_POSTS.SORT_ORDER.asc())).as("display_order")
                )
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .where(SERIES_POSTS.SERIES_ID.eq(seriesId))
                .and(POSTS.IS_PUBLISHED_YN.eq("Y"))
                .orderBy(SERIES_POSTS.SORT_ORDER.asc())
                .fetch();

        // 전체 포스트 수
        int totalCount = seriesPostsOrdered.size();

        // 현재 포스트의 display_order 찾기
        int currentOrder = 0;
        for (var record : seriesPostsOrdered) {
            if (record.get(SERIES_POSTS.POST_ID).equals(postId)) {
                currentOrder = record.get("display_order", Integer.class);
                break;
            }
        }

        // 이전/다음 포스트 찾기
        PostSeriesNavViewModel.NavItem prevItem = findPrevPost(seriesPostsOrdered, rawOrder);
        PostSeriesNavViewModel.NavItem nextItem = findNextPost(seriesPostsOrdered, rawOrder);

        // 결과 조립
        PostSeriesNavViewModel nav = new PostSeriesNavViewModel();
        nav.setSeriesId(seriesId);
        nav.setSeriesSlug(seriesBase.get("series_slug", String.class));
        nav.setSeriesName(seriesBase.get("series_name", String.class));
        nav.setSeriesStatus(seriesBase.get("series_status", String.class));
        nav.setTotalCount(totalCount);
        nav.setCurrentOrder(currentOrder);
        nav.setPrev(prevItem);
        nav.setNext(nextItem);

        if (thumbnailRecord != null) {
            nav.setThumbnailKey(thumbnailRecord.get(BLOBS.KEY));
            nav.setThumbnailMetadata(thumbnailRecord.get(BLOBS.METADATA));
        }

        thumbnailEnricher.enrich(nav);
        return nav;
    }

    /**
     * 관리자용 시리즈 목록 조회 (페이지네이션)
     * - 이름, 상태, 발행여부 필터 지원
     * - 각 시리즈별 포스트 수 포함
     * - 썸네일 정보 포함
     *
     * @param name 시리즈명 검색어 (부분 일치)
     * @param status 상태 필터 (PLAN, PROGRESS, COMPLETED)
     * @param isPublishedYn 발행 여부 필터 (Y/N)
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 시리즈 목록
     */
    public Pagination<AdminSeriesViewModel> getAdminSeriesWithPagination(
            String name,
            String status,
            String isPublishedYn,
            int page,
            int size
    ) {
        int offset = Pagination.getOffset(page, size);

        // 필터 조건 구성
        Condition condition = noCondition();
        if (name != null && !name.isEmpty()) {
            condition = condition.and(SERIES.NAME.likeIgnoreCase("%" + name + "%"));
        }
        if (status != null && !status.isEmpty()) {
            condition = condition.and(SERIES.STATUS.eq(status));
        }
        if (isPublishedYn != null && !isPublishedYn.isEmpty()) {
            condition = condition.and(SERIES.IS_PUBLISHED_YN.eq(isPublishedYn));
        }

        // 포스트 수 서브쿼리
        var postCountSubquery = select(SERIES_POSTS.SERIES_ID, count().as("post_count"))
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .groupBy(SERIES_POSTS.SERIES_ID)
                .asTable("post_counts");

        var thumbnail = thumbnailSubquery("series");

        // 시리즈 목록 조회
        var items = dsl.select(
                        SERIES.ID,
                        SERIES.NAME,
                        SERIES.SLUG,
                        SERIES.DESCRIPTION,
                        SERIES.STATUS,
                        SERIES.IS_PUBLISHED_YN,
                        SERIES.PUBLISHED_AT,
                        SERIES.CREATED_AT,
                        coalesce(postCountSubquery.field("post_count", Integer.class), 0).as("post_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES)
                .leftJoin(postCountSubquery).on(postCountSubquery.field("series_id", Long.class).eq(SERIES.ID))
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(SERIES.ID.cast(String.class)))
                .where(condition)
                .orderBy(SERIES.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(AdminSeriesViewModel.class);

        items.forEach(thumbnailEnricher::enrich);

        // 전체 개수 조회
        int totalCount = dsl.fetchCount(SERIES, condition);

        return Pagination.of(items, page, totalCount, null, size);
    }

    /**
     * ID로 시리즈 단건 조회 (관리자용)
     * - 포스트 수 및 썸네일 정보 포함
     *
     * @param id 시리즈 ID
     * @return 시리즈 정보
     * @throws NotFoundException 시리즈가 존재하지 않을 경우
     */
    public AdminSeriesViewModel getSeriesById(Long id) {
        // 포스트 수 서브쿼리
        var postCountSubquery = select(SERIES_POSTS.SERIES_ID, count().as("post_count"))
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .where(SERIES_POSTS.SERIES_ID.eq(id))
                .groupBy(SERIES_POSTS.SERIES_ID)
                .asTable("post_counts");

        // 썸네일 서브쿼리 (특정 ID만 조회)
        var thumbnail = select(
                ATTACHMENTS.RECORD_ID,
                BLOBS.KEY.as("thumbnail_key"),
                BLOBS.METADATA.as("thumbnail_metadata")
        )
                .from(ATTACHMENTS)
                .leftJoin(BLOBS).on(BLOBS.ID.eq(ATTACHMENTS.BLOB_ID))
                .where(ATTACHMENTS.RECORD_TYPE.eq("series"))
                .and(ATTACHMENTS.NAME.eq("thumbnail"))
                .and(ATTACHMENTS.RECORD_ID.eq(id.toString()))
                .asTable("thumbnail");

        var series = dsl.select(
                        SERIES.ID,
                        SERIES.NAME,
                        SERIES.SLUG,
                        SERIES.DESCRIPTION,
                        SERIES.STATUS,
                        SERIES.IS_PUBLISHED_YN,
                        SERIES.PUBLISHED_AT,
                        SERIES.CREATED_AT,
                        coalesce(postCountSubquery.field("post_count", Integer.class), 0).as("post_count"),
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES)
                .leftJoin(postCountSubquery).on(postCountSubquery.field("series_id", Long.class).eq(SERIES.ID))
                .leftJoin(thumbnail).on(DSL.trueCondition())
                .where(SERIES.ID.eq(id))
                .fetchOneInto(AdminSeriesViewModel.class);

        if (series == null) {
            throw new NotFoundException("시리즈를 찾을 수 없습니다.");
        }

        thumbnailEnricher.enrich(series);
        return series;
    }

    /**
     * 시리즈에 포함된 포스트 목록 조회 (관리자용)
     * - sort_order 순 정렬
     * - 각 포스트별 썸네일 정보 포함
     *
     * @param seriesId 시리즈 ID
     * @return 시리즈에 포함된 포스트 목록
     */
    public List<SeriesPostViewModel> getSeriesPostsById(Long seriesId) {
        var thumbnail = thumbnailSubquery("post");

        var posts = dsl.select(
                        SERIES_POSTS.ID,
                        SERIES_POSTS.POST_ID,
                        POSTS.TITLE.as("post_title"),
                        POSTS.SLUG.as("post_slug"),
                        SERIES_POSTS.SORT_ORDER,
                        SERIES_POSTS.CREATED_AT,
                        thumbnail.field("thumbnail_key", String.class).as("thumbnail_key"),
                        thumbnail.field("thumbnail_metadata", String.class).as("thumbnail_metadata")
                )
                .from(SERIES_POSTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(SERIES_POSTS.POST_ID))
                .leftJoin(thumbnail).on(thumbnail.field("record_id", String.class).eq(POSTS.ID.cast(String.class)))
                .where(SERIES_POSTS.SERIES_ID.eq(seriesId))
                .orderBy(SERIES_POSTS.SORT_ORDER.asc())
                .fetchInto(SeriesPostViewModel.class);

        posts.forEach(thumbnailEnricher::enrich);
        return posts;
    }

    /**
     * 시리즈에 포함되지 않은 포스트 목록 조회 (관리자용)
     * - 포스트 추가 시 선택 가능한 목록
     * - 제목 검색 지원
     * - 최대 20개 반환
     *
     * @param seriesId 시리즈 ID
     * @param keyword 제목 검색어 (부분 일치)
     * @return 시리즈에 포함되지 않은 포스트 목록
     */
    public List<AvailablePostViewModel> getPostsNotInSeries(Long seriesId, String keyword) {
        // 이미 시리즈에 포함된 포스트 ID들
        var existingPostIds = select(SERIES_POSTS.POST_ID)
                .from(SERIES_POSTS)
                .where(SERIES_POSTS.SERIES_ID.eq(seriesId));

        // 검색 조건
        Condition condition = POSTS.ID.notIn(existingPostIds);
        if (keyword != null && !keyword.isEmpty()) {
            condition = condition.and(POSTS.TITLE.likeIgnoreCase("%" + keyword + "%"));
        }

        return dsl.select(
                        POSTS.ID,
                        POSTS.TITLE,
                        POSTS.SLUG,
                        POSTS.PUBLISHED_AT
                )
                .from(POSTS)
                .where(condition)
                .orderBy(POSTS.CREATED_AT.desc())
                .limit(20)
                .fetchInto(AvailablePostViewModel.class);
    }

    // ========== Private Methods ==========

    /**
     * 썸네일 조회용 서브쿼리 생성
     * - attachments + blobs 조인
     * - record_type별 썸네일 조회
     *
     * @param recordType 레코드 타입 ("series" 또는 "post")
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
     * 이전 포스트 찾기
     * - sort_order가 현재보다 작은 것 중 가장 큰 것
     *
     * @param seriesPostsOrdered 정렬된 시리즈 포스트 목록
     * @param currentSortOrder 현재 포스트의 sort_order
     * @return 이전 포스트 정보 (없으면 null)
     */
    private PostSeriesNavViewModel.NavItem findPrevPost(
            org.jooq.Result<? extends Record> seriesPostsOrdered,
            Integer currentSortOrder
    ) {
        for (int i = seriesPostsOrdered.size() - 1; i >= 0; i--) {
            var record = seriesPostsOrdered.get(i);
            if (record.get(SERIES_POSTS.SORT_ORDER, Integer.class) < currentSortOrder) {
                PostSeriesNavViewModel.NavItem item = new PostSeriesNavViewModel.NavItem();
                item.setId(record.get(SERIES_POSTS.POST_ID));
                item.setSlug(record.get(POSTS.SLUG));
                item.setTitle(record.get(POSTS.TITLE));
                return item;
            }
        }
        return null;
    }

    /**
     * 다음 포스트 찾기
     * - sort_order가 현재보다 큰 것 중 가장 작은 것
     *
     * @param seriesPostsOrdered 정렬된 시리즈 포스트 목록
     * @param currentSortOrder 현재 포스트의 sort_order
     * @return 다음 포스트 정보 (없으면 null)
     */
    private PostSeriesNavViewModel.NavItem findNextPost(
            org.jooq.Result<? extends Record> seriesPostsOrdered,
            Integer currentSortOrder
    ) {
        for (var record : seriesPostsOrdered) {
            if (record.get(SERIES_POSTS.SORT_ORDER, Integer.class) > currentSortOrder) {
                PostSeriesNavViewModel.NavItem item = new PostSeriesNavViewModel.NavItem();
                item.setId(record.get(SERIES_POSTS.POST_ID));
                item.setSlug(record.get(POSTS.SLUG));
                item.setTitle(record.get(POSTS.TITLE));
                return item;
            }
        }
        return null;
    }
}
