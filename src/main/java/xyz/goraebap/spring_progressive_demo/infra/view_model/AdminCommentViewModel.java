package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCommentViewModel {
    private Long id;
    private String nickname;
    private String comment;
    private Integer avatarNo;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // 게시물 정보
    private Long postId;
    private String postTitle;
    private String postSlug;
    private String postType;

    public String getPostUrl() {
        if (postSlug == null) return null;
        return "patch-note".equals(postType) ? "/patch-notes/" + postSlug : "/posts/" + postSlug;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
