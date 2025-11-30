package xyz.goraebap.spring_progressive_demo.infra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.PostViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import xyz.goraebap.spring_progressive_demo.shared.config.R2Properties;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final R2Properties r2Properties;
    private final ObjectMapper objectMapper;
    private final PostViewMapper postViewMapper;

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

        posts.forEach(this::enrichThumbnail);

        return Pagination.of(posts, page, totalCount, sort);
    }

    private void enrichThumbnail(PostViewModel post) {
        if (post.getThumbnailKey() != null) {
            post.setThumbnailUrl(r2Properties.getPublicUrl(post.getThumbnailKey()));
        }

        if (post.getThumbnailMetadata() != null) {
            try {
                JsonNode metadata = objectMapper.readTree(post.getThumbnailMetadata());
                if (metadata.has("dominantColor")) {
                    post.setThumbnailDominantColor(metadata.get("dominantColor").asText());
                }
            } catch (Exception e) {
                log.warn("Failed to parse thumbnail metadata: {}", e.getMessage());
            }
        }
    }
}
