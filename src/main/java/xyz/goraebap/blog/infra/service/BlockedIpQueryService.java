package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.BlockedIpViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

import static jooq.Tables.BLOCKED_IPS;
import static org.jooq.impl.DSL.noCondition;

/**
 * 차단 IP 조회 서비스
 * - 관리자 페이지용 차단 IP 목록/상세 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class BlockedIpQueryService {

    private final DSLContext dsl;

    /**
     * 관리자용 차단 IP 목록 조회 (페이지네이션)
     * @param search 검색어 (IP 주소 또는 사유)
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 차단 IP 목록
     */
    public Pagination<BlockedIpViewModel> getBlockedIpsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;

        // 검색 조건: IP 주소 또는 사유에서 부분 일치 (대소문자 무시)
        Condition searchFilter = noCondition();
        if (search != null && !search.isEmpty()) {
            searchFilter = BLOCKED_IPS.IP_ADDRESS.likeIgnoreCase("%" + search + "%")
                    .or(BLOCKED_IPS.REASON.likeIgnoreCase("%" + search + "%"));
        }

        // 차단 IP 목록 조회
        var items = dsl.select(
                        BLOCKED_IPS.ID,
                        BLOCKED_IPS.IP_ADDRESS,
                        BLOCKED_IPS.REASON,
                        BLOCKED_IPS.BLOCKED_BY,
                        BLOCKED_IPS.EXPIRES_AT,
                        BLOCKED_IPS.IS_ACTIVE_YN,
                        BLOCKED_IPS.CREATED_AT,
                        BLOCKED_IPS.UPDATED_AT
                )
                .from(BLOCKED_IPS)
                .where(searchFilter)
                .orderBy(BLOCKED_IPS.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(BlockedIpViewModel.class);

        // 전체 개수 조회 (페이지네이션용)
        int totalCount = dsl.fetchCount(BLOCKED_IPS, searchFilter);

        return Pagination.of(items, page, totalCount, "desc", size);
    }

    /**
     * 차단 IP 상세 조회
     * @param id 차단 IP ID
     * @return 차단 IP 정보 (없으면 null)
     */
    public BlockedIpViewModel getById(Long id) {
        return dsl.select(
                        BLOCKED_IPS.ID,
                        BLOCKED_IPS.IP_ADDRESS,
                        BLOCKED_IPS.REASON,
                        BLOCKED_IPS.BLOCKED_BY,
                        BLOCKED_IPS.EXPIRES_AT,
                        BLOCKED_IPS.IS_ACTIVE_YN,
                        BLOCKED_IPS.CREATED_AT,
                        BLOCKED_IPS.UPDATED_AT
                )
                .from(BLOCKED_IPS)
                .where(BLOCKED_IPS.ID.eq(id))
                .fetchOneInto(BlockedIpViewModel.class);
    }
}
