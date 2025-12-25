package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.mapper.TagViewMapper;
import xyz.goraebap.blog.infra.view_model.AdminTagViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;
import xyz.goraebap.blog.shared.exception.NotFoundException;

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

    public AdminTagViewModel getTagById(Long id) {
        var tag = tagViewMapper.findTagById(id);
        if (tag == null) {
            throw new NotFoundException("태그를 찾을 수 없습니다.");
        }
        return tag;
    }
}
