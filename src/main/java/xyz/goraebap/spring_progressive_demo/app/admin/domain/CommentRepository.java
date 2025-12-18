package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    boolean existsByRequestId(String requestId);
}
