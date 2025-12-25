package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CuratedItemRepository extends JpaRepository<CuratedItemEntity, Long> {

    Optional<CuratedItemEntity> findByGuid(String guid);

    Optional<CuratedItemEntity> findByLink(String link);

    void deleteBySourceId(Long sourceId);

    @Modifying
    @Query("DELETE FROM CuratedItemEntity e WHERE e.createdAt < :threshold")
    int deleteOldItems(@Param("threshold") LocalDateTime threshold);

    long countBySourceId(Long sourceId);
}
