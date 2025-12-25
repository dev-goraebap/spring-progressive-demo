package xyz.goraebap.blog.infra.view_model;

import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T> {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private final List<T> items;
    private final int currentPage;
    private final int nextPage;
    private final boolean hasMore;
    private final String orderType;
    private final int pageSize;
    private final int totalCount;
    private final int totalPages;

    private Pagination(List<T> items, int currentPage, int totalCount, String orderType, int pageSize) {
        this.items = items;
        this.currentPage = currentPage;
        this.nextPage = currentPage + 1;
        this.hasMore = ((currentPage - 1) * pageSize + items.size()) < totalCount;
        this.orderType = orderType;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
    }

    public static <T> Pagination<T> of(List<T> items, int page, int totalCount, String orderType) {
        return new Pagination<>(items, page, totalCount, orderType, DEFAULT_PAGE_SIZE);
    }

    public static <T> Pagination<T> of(List<T> items, int page, int totalCount, String orderType, int pageSize) {
        return new Pagination<>(items, page, totalCount, orderType, pageSize);
    }

    public static int getOffset(int page) {
        return getOffset(page, DEFAULT_PAGE_SIZE);
    }

    public static int getOffset(int page, int pageSize) {
        return (page - 1) * pageSize;
    }
}
