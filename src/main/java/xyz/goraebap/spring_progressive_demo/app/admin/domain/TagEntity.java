package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description = "";

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static TagEntity create(String name, Long userId) {
        var entity = new TagEntity();
        entity.name = name;
        entity.userId = userId;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }

    public void update(String name) {
        this.name = name;
    }
}
