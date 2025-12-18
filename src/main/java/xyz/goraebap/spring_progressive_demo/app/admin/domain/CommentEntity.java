package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String requestId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private Integer avatarNo;

    @Column(nullable = false)
    private Long postId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public static CommentEntity create(String requestId, Long postId, String nickname, String comment, Integer avatarNo) {
        CommentEntity c = new CommentEntity();
        c.requestId = requestId;
        c.postId = postId;
        c.nickname = nickname;
        c.comment = comment;
        c.avatarNo = avatarNo;
        return c;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
