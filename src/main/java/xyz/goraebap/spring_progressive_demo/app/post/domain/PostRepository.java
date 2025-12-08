package xyz.goraebap.spring_progressive_demo.app.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findBySlug(String slug);
}
