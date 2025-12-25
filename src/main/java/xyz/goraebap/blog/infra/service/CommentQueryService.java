package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.mapper.CommentViewMapper;
import xyz.goraebap.blog.infra.view_model.AdminCommentViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentViewMapper commentViewMapper;

    public Pagination<AdminCommentViewModel> getAdminCommentsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;
        var items = commentViewMapper.findAdminComments(search, size, offset);
        int totalCount = commentViewMapper.countAdminComments(search);
        return Pagination.of(items, page, totalCount, "desc", size);
    }
}
