package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.mapper.CurationViewMapper;
import xyz.goraebap.blog.infra.view_model.AdminCuratedItemViewModel;
import xyz.goraebap.blog.infra.view_model.AdminCuratedSourceViewModel;
import xyz.goraebap.blog.infra.view_model.CurationViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurationQueryService {

    private final CurationViewMapper curationViewMapper;

    private static final int PAGE_SIZE = 10;
    private static final int ADMIN_PAGE_SIZE = 20;

    // ===== 클라이언트 =====

    public List<CurationViewModel> getLatestItems(int limit) {
        return curationViewMapper.findItems("publishedAt", "DESC", limit, 0);
    }

    public Pagination<CurationViewModel> getItemsWithPagination(int page, String sort) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page, PAGE_SIZE);
        int totalCount = curationViewMapper.countItems();
        var items = curationViewMapper.findItems(sortBy, sortDir, PAGE_SIZE, offset);

        return Pagination.of(items, page, totalCount, sort, PAGE_SIZE);
    }

    // ===== 관리자: 소스 =====

    public List<AdminCuratedSourceViewModel> getAllSources() {
        return curationViewMapper.findAllSources();
    }

    public AdminCuratedSourceViewModel getSourceById(Long id) {
        var source = curationViewMapper.findSourceById(id);
        if (source == null) {
            throw new NotFoundException("소스를 찾을 수 없습니다.");
        }
        return source;
    }

    // ===== 관리자: 항목 =====

    public Pagination<AdminCuratedItemViewModel> getAdminItemsWithPagination(
            int page, String sort, Long sourceId) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page, ADMIN_PAGE_SIZE);
        int totalCount = curationViewMapper.countAdminItems(sourceId);
        var items = curationViewMapper.findAdminItems(sourceId, sortBy, sortDir, ADMIN_PAGE_SIZE, offset);

        return Pagination.of(items, page, totalCount, sort, ADMIN_PAGE_SIZE);
    }

    // ===== 통계 =====

    public Map<String, Object> getStats() {
        return curationViewMapper.getStats();
    }
}
