package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "series_posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"series_id", "post_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeriesPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "series_id", nullable = false)
    private Long seriesId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "sort_order", nullable = false, columnDefinition = "integer default 999")
    private Integer sortOrder = 999;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static SeriesPostEntity create(Long seriesId, Long postId) {
        var entity = new SeriesPostEntity();
        entity.seriesId = seriesId;
        entity.postId = postId;
        entity.sortOrder = 999;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }
}
