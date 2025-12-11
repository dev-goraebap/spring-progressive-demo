package xyz.goraebap.spring_progressive_demo.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminTagViewModel {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private int postCount;
}
