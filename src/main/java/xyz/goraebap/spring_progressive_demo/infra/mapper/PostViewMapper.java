package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;

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
}
