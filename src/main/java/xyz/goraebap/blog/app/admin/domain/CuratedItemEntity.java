package xyz.goraebap.blog.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "curated_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CuratedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String link;

    @Column(nullable = false, columnDefinition = "text")
    private String guid;

    @Column(columnDefinition = "text")
    private String snippet;

    @Column(name = "pub_date", nullable = false)
    private LocalDateTime pubDate;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CuratedItemEntity create(String title, String link, String guid, String snippet,
                                           LocalDateTime pubDate, String source, Long sourceId) {
        var entity = new CuratedItemEntity();
        entity.title = title;
        entity.link = link;
        entity.guid = guid;
        entity.snippet = snippet != null && snippet.length() > 500 ? snippet.substring(0, 500) : snippet;
        entity.pubDate = pubDate != null ? pubDate : LocalDateTime.now();
        entity.source = source;
        entity.sourceId = sourceId;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}
