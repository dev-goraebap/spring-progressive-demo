package xyz.goraebap.blog.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.blog.infra.view_model.AdminTagViewModel;

import java.util.List;

@Mapper
public interface TagViewMapper {

    List<AdminTagViewModel> findAdminTags(
            @Param("name") String name,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countAdminTags(@Param("name") String name);

    AdminTagViewModel findTagById(@Param("id") Long id);
}
