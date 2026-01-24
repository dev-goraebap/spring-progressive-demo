package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.FcmSubscriptionViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

import java.util.List;
import java.util.Map;

import static jooq.Tables.FCM_SUBSCRIPTIONS;
import static org.jooq.impl.DSL.*;

/**
 * FCM 구독 조회 서비스
 * - 관리자 페이지용 구독자 목록 조회
 * - 브라우저/OS/기기 타입별 통계 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class FcmSubscriptionQueryService {

    private final DSLContext dsl;

    /**
     * 관리자용 FCM 구독자 목록 조회 (페이지네이션)
     * @param search 검색어 (IP 주소, 브라우저, OS)
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 구독자 목록
     */
    public Pagination<FcmSubscriptionViewModel> getSubscriptionsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;

        // 검색 조건: IP 주소, 브라우저, OS에서 부분 일치 (대소문자 무시)
        Condition searchFilter = noCondition();
        if (search != null && !search.isEmpty()) {
            searchFilter = FCM_SUBSCRIPTIONS.IP_ADDRESS.likeIgnoreCase("%" + search + "%")
                    .or(FCM_SUBSCRIPTIONS.BROWSER.likeIgnoreCase("%" + search + "%"))
                    .or(FCM_SUBSCRIPTIONS.OS.likeIgnoreCase("%" + search + "%"));
        }

        // 구독자 목록 조회
        var items = dsl.select(
                        FCM_SUBSCRIPTIONS.ID,
                        FCM_SUBSCRIPTIONS.TOKEN,
                        FCM_SUBSCRIPTIONS.IP_ADDRESS,
                        FCM_SUBSCRIPTIONS.BROWSER,
                        FCM_SUBSCRIPTIONS.OS,
                        FCM_SUBSCRIPTIONS.DEVICE_TYPE,
                        FCM_SUBSCRIPTIONS.CREATED_AT
                )
                .from(FCM_SUBSCRIPTIONS)
                .where(searchFilter)
                .orderBy(FCM_SUBSCRIPTIONS.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(FcmSubscriptionViewModel.class);

        // 전체 개수 조회 (페이지네이션용)
        int totalCount = dsl.fetchCount(FCM_SUBSCRIPTIONS, searchFilter);

        return Pagination.of(items, page, totalCount, "desc", size);
    }

    /**
     * 전체 구독자 수 조회
     * @return 전체 구독자 수
     */
    public int getTotalCount() {
        return dsl.fetchCount(FCM_SUBSCRIPTIONS);
    }

    /**
     * 브라우저별 구독자 통계
     * @return 브라우저명(name)과 개수(count) 목록
     */
    public List<Map<String, Object>> getStatsByBrowser() {
        return dsl.select(
                        coalesce(FCM_SUBSCRIPTIONS.BROWSER, "알 수 없음").as("name"),
                        count().as("count")
                )
                .from(FCM_SUBSCRIPTIONS)
                .groupBy(FCM_SUBSCRIPTIONS.BROWSER)
                .orderBy(count().desc())
                .fetchMaps();
    }

    /**
     * OS별 구독자 통계
     * @return OS명(name)과 개수(count) 목록
     */
    public List<Map<String, Object>> getStatsByOs() {
        return dsl.select(
                        coalesce(FCM_SUBSCRIPTIONS.OS, "알 수 없음").as("name"),
                        count().as("count")
                )
                .from(FCM_SUBSCRIPTIONS)
                .groupBy(FCM_SUBSCRIPTIONS.OS)
                .orderBy(count().desc())
                .fetchMaps();
    }

    /**
     * 기기 타입별 구독자 통계
     * @return 기기 타입명(name)과 개수(count) 목록
     */
    public List<Map<String, Object>> getStatsByDeviceType() {
        return dsl.select(
                        coalesce(FCM_SUBSCRIPTIONS.DEVICE_TYPE, "알 수 없음").as("name"),
                        count().as("count")
                )
                .from(FCM_SUBSCRIPTIONS)
                .groupBy(FCM_SUBSCRIPTIONS.DEVICE_TYPE)
                .orderBy(count().desc())
                .fetchMaps();
    }
}
