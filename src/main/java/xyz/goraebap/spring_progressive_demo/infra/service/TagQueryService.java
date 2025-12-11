package xyz.goraebap.spring_progressive_demo.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.TagViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminTagViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

@Service
@RequiredArgsConstructor
public class TagQueryService {

    private final TagViewMapper tagViewMapper;

    public Pagination<AdminTagViewModel> getAdminTagsWithPagination(
            String name,
            int page,
            int size
    ) {
        int offset = Pagination.getOffset(page, size);
        var items = tagViewMapper.findAdminTags(name, size, offset);
        int totalCount = tagViewMapper.countAdminTags(name);
        return Pagination.of(items, page, totalCount, null, size);
    }
}
