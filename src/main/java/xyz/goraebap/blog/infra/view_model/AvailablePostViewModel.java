package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailablePostViewModel {
    private Long id;
    private String title;
    private String slug;
    private LocalDateTime publishedAt;
}
