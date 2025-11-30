package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T> {
    public static final int PAGE_SIZE = 5;

    private final List<T> items;
    private final int currentPage;
    private final int nextPage;
    private final boolean hasMore;
    private final String orderType;

    private Pagination(List<T> items, int currentPage, int totalCount, String orderType) {
        this.items = items;
        this.currentPage = currentPage;
        this.nextPage = currentPage + 1;
        this.hasMore = ((currentPage - 1) * PAGE_SIZE + items.size()) < totalCount;
        this.orderType = orderType;
    }

    public static <T> Pagination<T> of(List<T> items, int page, int totalCount, String orderType) {
        return new Pagination<>(items, page, totalCount, orderType);
    }

    /**
     * 클라이언트 페이지(1부터 시작)를 DB offset으로 변환
     */
    public static int getOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }
}
