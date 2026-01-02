package xyz.goraebap.blog.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.blog.infra.view_model.AdminPostViewModel;
import xyz.goraebap.blog.infra.view_model.PostViewModel;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostViewMapper {
    List<PostViewModel> findPosts(
        @Param("postType") String postType,
        @Param("sortBy") String sortBy,
        @Param("sortDir") String sortDir,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countPosts(@Param("postType") String postType);

    PostViewModel findPostBySlug(@Param("slug") String slug, @Param("postType") String postType);

    PostViewModel findLatestByPostType(@Param("postType") String postType);

    // Admin
    List<AdminPostViewModel> findAdminPosts(
        @Param("postType") String postType,
        @Param("isPublishedYn") String isPublishedYn,
        @Param("title") String title,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countAdminPosts(
        @Param("postType") String postType,
        @Param("isPublishedYn") String isPublishedYn,
        @Param("title") String title
    );

    AdminPostViewModel findAdminPostById(@Param("id") Long id);

    boolean existsPublishedAfter(@Param("postType") String postType, @Param("after") LocalDateTime after);
}
