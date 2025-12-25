package xyz.goraebap.blog.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.goraebap.blog.app.admin.dto.AdminCuratedSourceFormRequest;

import java.time.LocalDateTime;

@Entity
@Table(name = "curated_sources")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CuratedSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(name = "is_active_yn", nullable = false, length = 1)
    private String isActiveYn = "Y";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static CuratedSourceEntity create(AdminCuratedSourceFormRequest req) {
        var entity = new CuratedSourceEntity();
        entity.name = req.getName();
        entity.url = req.getUrl();
        entity.isActiveYn = req.getIsActiveYn() != null ? req.getIsActiveYn() : "Y";
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    public void update(AdminCuratedSourceFormRequest req) {
        this.name = req.getName();
        this.url = req.getUrl();
        if (req.getIsActiveYn() != null) {
            this.isActiveYn = req.getIsActiveYn();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleActive() {
        this.isActiveYn = "Y".equals(this.isActiveYn) ? "N" : "Y";
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return "Y".equals(this.isActiveYn);
    }
}
