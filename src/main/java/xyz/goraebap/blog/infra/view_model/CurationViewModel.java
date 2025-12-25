package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CurationViewModel {
    private Long id;
    private String title;
    private String link;
    private String snippet;
    private LocalDateTime pubDate;
    private String source;
}
