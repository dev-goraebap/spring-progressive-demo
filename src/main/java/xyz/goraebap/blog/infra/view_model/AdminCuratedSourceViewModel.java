package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCuratedSourceViewModel {
    private Long id;
    private String name;
    private String url;
    private String isActiveYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int itemCount;

    public boolean isActive() {
        return "Y".equals(isActiveYn);
    }
}
