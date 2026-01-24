package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.AdminCuratedItemViewModel;
import xyz.goraebap.blog.infra.view_model.AdminCuratedSourceViewModel;
import xyz.goraebap.blog.infra.view_model.CurationViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jooq.Tables.CURATED_ITEMS;
import static jooq.Tables.CURATED_SOURCES;
import static org.jooq.impl.DSL.*;

/**
 * 큐레이션 조회 서비스
 * - 클라이언트용 큐레이션 항목 목록 조회
 * - 관리자용 소스/항목 관리
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class CurationQueryService {

    private final DSLContext dsl;

    private static final int PAGE_SIZE = 10;
    private static final int ADMIN_PAGE_SIZE = 20;

    // ===== 클라이언트 =====

    /**
     * 최신 큐레이션 항목 조회
     * @param limit 조회할 개수
     * @return 최신 항목 목록 (발행일시 내림차순)
     */
    public List<CurationViewModel> getLatestItems(int limit) {
        return dsl.select(
                        CURATED_ITEMS.ID,
                        CURATED_ITEMS.TITLE,
                        CURATED_ITEMS.LINK,
                        CURATED_ITEMS.SNIPPET,
                        CURATED_ITEMS.PUB_DATE,
                        CURATED_ITEMS.SOURCE
                )
                .from(CURATED_ITEMS)
                .orderBy(CURATED_ITEMS.PUB_DATE.desc())
                .limit(limit)
                .fetchInto(CurationViewModel.class);
    }

    /**
     * 클라이언트용 큐레이션 항목 목록 조회 (페이지네이션)
     * @param page 페이지 번호
     * @param sort 정렬 기준 (예: "publishedAt,DESC")
     * @return 페이지네이션된 항목 목록
     */
    public Pagination<CurationViewModel> getItemsWithPagination(int page, String sort) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page, PAGE_SIZE);

        // 동적 정렬 필드 결정
        SortField<?> orderField = "DESC".equals(sortDir)
                ? CURATED_ITEMS.PUB_DATE.desc()
                : CURATED_ITEMS.PUB_DATE.asc();

        var items = dsl.select(
                        CURATED_ITEMS.ID,
                        CURATED_ITEMS.TITLE,
                        CURATED_ITEMS.LINK,
                        CURATED_ITEMS.SNIPPET,
                        CURATED_ITEMS.PUB_DATE,
                        CURATED_ITEMS.SOURCE
                )
                .from(CURATED_ITEMS)
                .orderBy(orderField)
                .limit(PAGE_SIZE)
                .offset(offset)
                .fetchInto(CurationViewModel.class);

        int totalCount = dsl.fetchCount(CURATED_ITEMS);

        return Pagination.of(items, page, totalCount, sort, PAGE_SIZE);
    }

    // ===== 관리자: 소스 =====

    /**
     * 관리자용 소스 목록 조회 (항목 수 포함)
     * @return 소스 목록
     */
    public List<AdminCuratedSourceViewModel> getAllSources() {
        return dsl.select(
                        CURATED_SOURCES.ID,
                        CURATED_SOURCES.NAME,
                        CURATED_SOURCES.URL,
                        CURATED_SOURCES.IS_ACTIVE_YN,
                        CURATED_SOURCES.CREATED_AT,
                        CURATED_SOURCES.UPDATED_AT,
                        coalesce(count(CURATED_ITEMS.ID), 0).as("item_count")
                )
                .from(CURATED_SOURCES)
                .leftJoin(CURATED_ITEMS).on(CURATED_ITEMS.SOURCE_ID.eq(CURATED_SOURCES.ID))
                .groupBy(
                        CURATED_SOURCES.ID,
                        CURATED_SOURCES.NAME,
                        CURATED_SOURCES.URL,
                        CURATED_SOURCES.IS_ACTIVE_YN,
                        CURATED_SOURCES.CREATED_AT,
                        CURATED_SOURCES.UPDATED_AT
                )
                .orderBy(CURATED_SOURCES.CREATED_AT.desc())
                .fetchInto(AdminCuratedSourceViewModel.class);
    }

    /**
     * 관리자용 소스 상세 조회 (항목 수 포함)
     * @param id 소스 ID
     * @return 소스 정보
     * @throws NotFoundException 소스가 없는 경우
     */
    public AdminCuratedSourceViewModel getSourceById(Long id) {
        var source = dsl.select(
                        CURATED_SOURCES.ID,
                        CURATED_SOURCES.NAME,
                        CURATED_SOURCES.URL,
                        CURATED_SOURCES.IS_ACTIVE_YN,
                        CURATED_SOURCES.CREATED_AT,
                        CURATED_SOURCES.UPDATED_AT,
                        coalesce(count(CURATED_ITEMS.ID), 0).as("item_count")
                )
                .from(CURATED_SOURCES)
                .leftJoin(CURATED_ITEMS).on(CURATED_ITEMS.SOURCE_ID.eq(CURATED_SOURCES.ID))
                .where(CURATED_SOURCES.ID.eq(id))
                .groupBy(
                        CURATED_SOURCES.ID,
                        CURATED_SOURCES.NAME,
                        CURATED_SOURCES.URL,
                        CURATED_SOURCES.IS_ACTIVE_YN,
                        CURATED_SOURCES.CREATED_AT,
                        CURATED_SOURCES.UPDATED_AT
                )
                .fetchOneInto(AdminCuratedSourceViewModel.class);

        if (source == null) {
            throw new NotFoundException("소스를 찾을 수 없습니다.");
        }
        return source;
    }

    // ===== 관리자: 항목 =====

    /**
     * 관리자용 항목 목록 조회 (페이지네이션)
     * @param page 페이지 번호
     * @param sort 정렬 기준 (예: "pubDate,DESC")
     * @param sourceId 소스 ID (null이면 전체)
     * @return 페이지네이션된 항목 목록
     */
    public Pagination<AdminCuratedItemViewModel> getAdminItemsWithPagination(
            int page, String sort, Long sourceId) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page, ADMIN_PAGE_SIZE);

        // 소스 ID 필터 조건
        Condition sourceFilter = noCondition();
        if (sourceId != null) {
            sourceFilter = CURATED_ITEMS.SOURCE_ID.eq(sourceId);
        }

        // 동적 정렬 필드 결정
        SortField<?> orderField;
        if ("pubDate".equals(sortBy)) {
            orderField = "DESC".equals(sortDir)
                    ? CURATED_ITEMS.PUB_DATE.desc()
                    : CURATED_ITEMS.PUB_DATE.asc();
        } else {
            orderField = "DESC".equals(sortDir)
                    ? CURATED_ITEMS.CREATED_AT.desc()
                    : CURATED_ITEMS.CREATED_AT.asc();
        }

        var items = dsl.select(
                        CURATED_ITEMS.ID,
                        CURATED_ITEMS.TITLE,
                        CURATED_ITEMS.LINK,
                        CURATED_ITEMS.SNIPPET,
                        CURATED_ITEMS.PUB_DATE,
                        CURATED_ITEMS.SOURCE,
                        CURATED_ITEMS.SOURCE_ID,
                        CURATED_ITEMS.CREATED_AT
                )
                .from(CURATED_ITEMS)
                .where(sourceFilter)
                .orderBy(orderField)
                .limit(ADMIN_PAGE_SIZE)
                .offset(offset)
                .fetchInto(AdminCuratedItemViewModel.class);

        int totalCount = dsl.fetchCount(CURATED_ITEMS, sourceFilter);

        return Pagination.of(items, page, totalCount, sort, ADMIN_PAGE_SIZE);
    }

    // ===== 통계 =====

    /**
     * 큐레이션 통계 조회
     * @return 전체 소스 수, 활성 소스 수, 전체 항목 수
     */
    public Map<String, Object> getStats() {
        int totalSources = dsl.fetchCount(CURATED_SOURCES);
        int activeSources = dsl.fetchCount(CURATED_SOURCES, CURATED_SOURCES.IS_ACTIVE_YN.eq("Y"));
        int totalItems = dsl.fetchCount(CURATED_ITEMS);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSources", totalSources);
        stats.put("activeSources", activeSources);
        stats.put("totalItems", totalItems);
        return stats;
    }
}
