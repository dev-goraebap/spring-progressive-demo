package xyz.goraebap.spring_progressive_demo.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.CurationViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurationQueryService {

    private final CurationViewMapper curationViewMapper;

    private static final int PAGE_SIZE = 10;

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
}
