package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesPostRepository extends JpaRepository<SeriesPostEntity, Long> {

    Optional<SeriesPostEntity> findBySeriesIdAndPostId(Long seriesId, Long postId);

    List<SeriesPostEntity> findBySeriesIdOrderBySortOrder(Long seriesId);

    List<SeriesPostEntity> findByIdIn(List<Long> ids);

    @Modifying
    @Query("DELETE FROM SeriesPostEntity sp WHERE sp.seriesId = :seriesId AND sp.postId = :postId")
    void deleteBySeriesIdAndPostId(Long seriesId, Long postId);

    @Modifying
    @Query("DELETE FROM SeriesPostEntity sp WHERE sp.seriesId = :seriesId")
    void deleteBySeriesId(Long seriesId);
}
