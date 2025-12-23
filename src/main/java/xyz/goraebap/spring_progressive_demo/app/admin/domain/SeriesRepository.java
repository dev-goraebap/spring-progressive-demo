package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {
    Optional<SeriesEntity> findByName(String name);
    Optional<SeriesEntity> findBySlug(String slug);
}
