package xyz.goraebap.blog.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.blog.infra.view_model.*;

import java.util.List;

@Mapper
public interface SeriesViewMapper {
    List<SeriesViewModel> findAllSeries();

    SeriesDetailViewModel findSeriesWithPosts(@Param("slug") String slug);

    PostSeriesNavViewModel findSeriesNavByPostId(@Param("postId") Long postId);

    List<AdminSeriesViewModel> findAdminSeries(
            @Param("name") String name,
            @Param("status") String status,
            @Param("isPublishedYn") String isPublishedYn,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countAdminSeries(
            @Param("name") String name,
            @Param("status") String status,
            @Param("isPublishedYn") String isPublishedYn
    );

    AdminSeriesViewModel findSeriesById(@Param("id") Long id);

    List<SeriesPostViewModel> findSeriesPosts(@Param("seriesId") Long seriesId);

    List<AvailablePostViewModel> findPostsNotInSeries(
            @Param("seriesId") Long seriesId,
            @Param("keyword") String keyword
    );
}
