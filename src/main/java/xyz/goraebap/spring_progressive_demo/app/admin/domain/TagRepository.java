package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
    boolean existsByName(String name);
}
