package xyz.goraebap.spring_progressive_demo.app.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByRequestId(String requestId);
}
