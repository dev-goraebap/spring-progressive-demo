package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentViewModel {
    private Long id;
    private String nickname;
    private String comment;
    private Integer avatarNo;
    private LocalDateTime createdAt;
}
