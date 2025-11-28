package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;

import java.util.List;

@Mapper
public interface PostViewMapper {
    List<PostViewModel> findPosts(
        @Param("orderType") String orderType,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countPosts();
}
