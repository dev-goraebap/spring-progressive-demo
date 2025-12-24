package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminCommentViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.CommentViewModel;

import java.util.List;

@Mapper
public interface CommentViewMapper {
    List<CommentViewModel> findByPostSlug(@Param("postSlug") String postSlug);

    int countByPostSlug(@Param("postSlug") String postSlug);

    List<AdminCommentViewModel> findAdminComments(
            @Param("search") String search,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countAdminComments(@Param("search") String search);
}
