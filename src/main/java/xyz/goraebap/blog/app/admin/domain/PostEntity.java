package xyz.goraebap.blog.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.goraebap.blog.app.admin.dto.AdminPostFormRequest;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {

    private static final Pattern H1_PATTERN = Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern P_PATTERN = Pattern.compile("<p[^>]*>(.*?)</p>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_type", nullable = false)
    private String postType;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

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

    public static PostEntity create(AdminPostFormRequest req, Long userId) {
        var entity = new PostEntity();
        entity.slug = (req.getSlug() != null && !req.getSlug().isBlank()) ? req.getSlug() : UUID.randomUUID().toString();
        entity.content = req.getContent();
        entity.title = extractTitle(req.getContent());
        entity.summary = extractSummary(req.getContent());
        entity.postType = req.getPostType() != null ? req.getPostType() : "post";
        entity.isPublishedYn = req.getIsPublishedYn() != null ? req.getIsPublishedYn() : "N";
        entity.publishedAt = req.getPublishedAt() != null ? req.getPublishedAt() : LocalDateTime.now();
        entity.userId = userId;
        entity.viewCount = 0;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    public void update(AdminPostFormRequest req) {
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            this.slug = req.getSlug();
        }
        this.content = req.getContent();
        this.title = extractTitle(req.getContent());
        this.summary = extractSummary(req.getContent());
        if (req.getPostType() != null) this.postType = req.getPostType();
        if (req.getIsPublishedYn() != null) this.isPublishedYn = req.getIsPublishedYn();
        if (req.getPublishedAt() != null) this.publishedAt = req.getPublishedAt();
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }

    private static String extractTitle(String content) {
        if (content == null || content.isBlank()) return "제목 없음";

        Matcher matcher = H1_PATTERN.matcher(content);
        if (matcher.find()) {
            String title = stripTags(matcher.group(1)).trim();
            return title.isEmpty() ? "제목 없음" : title;
        }
        return "제목 없음";
    }

    private static String extractSummary(String content) {
        if (content == null || content.isBlank()) return "";

        Matcher matcher = P_PATTERN.matcher(content);
        if (matcher.find()) {
            String summary = stripTags(matcher.group(1)).trim();
            if (summary.length() > 500) {
                summary = summary.substring(0, 497) + "...";
            }
            return summary;
        }
        return "";
    }

    private static String stripTags(String html) {
        if (html == null) return "";
        String text = TAG_PATTERN.matcher(html).replaceAll("");
        // HTML 엔티티 변환
        text = text.replace("&nbsp;", " ")
                   .replace("&amp;", "&")
                   .replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&quot;", "\"")
                   .replace("&#39;", "'");
        // 연속 공백 정리
        return text.replaceAll("\\s+", " ").trim();
    }
}
