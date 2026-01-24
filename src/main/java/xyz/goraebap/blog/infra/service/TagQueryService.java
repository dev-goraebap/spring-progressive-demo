package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.AdminTagViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import static jooq.Tables.*;
import static org.jooq.impl.DSL.*;

/**
 * 태그 조회 서비스
 * - 관리자 페이지용 태그 목록/상세 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class TagQueryService {

    private final DSLContext dsl;

    /**
     * 관리자용 태그 목록 조회 (페이지네이션)
     * - 태그명 검색 지원 (대소문자 무시)
     * - 각 태그별 연결된 게시물 수 포함
     *
     * @param name 검색할 태그명 (null 또는 빈 문자열이면 전체 조회)
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 태그 목록
     */
    public Pagination<AdminTagViewModel> getAdminTagsWithPagination(
            String name,
            int page,
            int size
    ) {
        int offset = Pagination.getOffset(page, size);

        // 검색 조건: 태그명 부분 일치 (대소문자 무시)
        Condition nameFilter = noCondition();
        if (name != null && !name.isEmpty()) {
            nameFilter = TAGS.NAME.likeIgnoreCase("%" + name + "%");
        }

        // 태그 목록 조회 (게시물 수 포함)
        var items = dsl.select(
                        TAGS.ID,
                        TAGS.NAME,
                        TAGS.CREATED_AT,
                        coalesce(count(POST_TAGS.TAG_ID), 0).as("post_count")
                )
                .from(TAGS)
                .leftJoin(POST_TAGS).on(POST_TAGS.TAG_ID.eq(TAGS.ID))
                .where(nameFilter)
                .groupBy(TAGS.ID, TAGS.NAME, TAGS.CREATED_AT)
                .orderBy(TAGS.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(AdminTagViewModel.class);

        // 전체 개수 조회 (페이지네이션용)
        int totalCount = dsl.fetchCount(TAGS, nameFilter);

        return Pagination.of(items, page, totalCount, null, size);
    }

    /**
     * ID로 태그 단건 조회
     * - 연결된 게시물 수 포함
     *
     * @param id 태그 ID
     * @return 태그 정보
     * @throws NotFoundException 태그가 존재하지 않을 경우
     */
    public AdminTagViewModel getTagById(Long id) {
        var tag = dsl.select(
                        TAGS.ID,
                        TAGS.NAME,
                        TAGS.CREATED_AT,
                        coalesce(count(POST_TAGS.TAG_ID), 0).as("post_count")
                )
                .from(TAGS)
                .leftJoin(POST_TAGS).on(POST_TAGS.TAG_ID.eq(TAGS.ID))
                .where(TAGS.ID.eq(id))
                .groupBy(TAGS.ID, TAGS.NAME, TAGS.CREATED_AT)
                .fetchOneInto(AdminTagViewModel.class);

        if (tag == null) {
            throw new NotFoundException("태그를 찾을 수 없습니다.");
        }
        return tag;
    }
}
