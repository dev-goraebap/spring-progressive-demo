package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminSeriesViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostSeriesNavViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesDetailViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel;

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
}
