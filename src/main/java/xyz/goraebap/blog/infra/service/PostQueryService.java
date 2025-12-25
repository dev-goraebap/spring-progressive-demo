package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.mapper.PostViewMapper;
import xyz.goraebap.blog.infra.view_model.AdminPostViewModel;
import xyz.goraebap.blog.infra.view_model.PostViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostViewMapper postViewMapper;
    private final ThumbnailEnricher thumbnailEnricher;

    public Pagination<PostViewModel> getPostsWithPagination(int page, String sort) {
        return getPostsWithPagination(page, sort, "post");
    }

    public Pagination<PostViewModel> getPostsWithPagination(int page, String sort, String postType) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1].toUpperCase() : "DESC";

        int offset = Pagination.getOffset(page);
        int totalCount = postViewMapper.countPosts(postType);
        var posts = postViewMapper.findPosts(postType, sortBy, sortDir, Pagination.DEFAULT_PAGE_SIZE, offset);

        posts.forEach(thumbnailEnricher::enrich);

        return Pagination.of(posts, page, totalCount, sort);
    }

    public PostViewModel getPostBySlug(String slug) {
        return getPostBySlug(slug, "post");
    }

    public PostViewModel getPostBySlug(String slug, String postType) {
        var post = postViewMapper.findPostBySlug(slug, postType);
        if (post != null) {
            thumbnailEnricher.enrich(post);
        }
        return post;
    }

    public PostViewModel getLatestPatchNote() {
        var post = postViewMapper.findLatestByPostType("patch-note");
        if (post != null) {
            thumbnailEnricher.enrich(post);
        }
        return post;
    }

    // Admin
    public Pagination<AdminPostViewModel> getAdminPostsWithPagination(
            String postType, String isPublishedYn, String title, int page, int size) {
        int offset = Pagination.getOffset(page, size);
        int totalCount = postViewMapper.countAdminPosts(postType, isPublishedYn, title);
        var posts = postViewMapper.findAdminPosts(postType, isPublishedYn, title, size, offset);
        posts.forEach(thumbnailEnricher::enrich);
        return Pagination.of(posts, page, totalCount, null, size);
    }

    public AdminPostViewModel getPostById(Long id) {
        var post = postViewMapper.findAdminPostById(id);
        if (post != null) {
            thumbnailEnricher.enrich(post);
        }
        return post;
    }
}
