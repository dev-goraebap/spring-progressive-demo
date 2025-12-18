package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {

    @Id
    private Long id;

    private String slug;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void incrementViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }
}
