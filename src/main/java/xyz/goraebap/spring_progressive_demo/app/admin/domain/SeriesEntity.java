package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminSeriesFormRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "series")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 20)
    private String status; // PLAN, PROGRESS, COMPLETE

    @Column(name = "is_published_yn", nullable = false)
    private String isPublishedYn;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static SeriesEntity create(AdminSeriesFormRequest req, Long userId) {
        var entity = new SeriesEntity();
        entity.name = req.getName();
        entity.slug = (req.getSlug() != null && !req.getSlug().isBlank())
                ? req.getSlug()
                : UUID.randomUUID().toString();
        entity.description = req.getDescription();
        entity.status = req.getStatus() != null ? req.getStatus() : "PLAN";
        entity.isPublishedYn = req.getIsPublishedYn() != null ? req.getIsPublishedYn() : "N";
        entity.publishedAt = req.getPublishedAt() != null ? req.getPublishedAt() : LocalDateTime.now();
        entity.userId = userId;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    public void update(AdminSeriesFormRequest req) {
        this.name = req.getName();
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            this.slug = req.getSlug();
        }
        this.description = req.getDescription();
        if (req.getStatus() != null) this.status = req.getStatus();
        if (req.getIsPublishedYn() != null) this.isPublishedYn = req.getIsPublishedYn();
        if (req.getPublishedAt() != null) this.publishedAt = req.getPublishedAt();
        this.updatedAt = LocalDateTime.now();
    }
}
