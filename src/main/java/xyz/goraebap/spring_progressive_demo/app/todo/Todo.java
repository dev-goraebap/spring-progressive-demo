package xyz.goraebap.spring_progressive_demo.app.todo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoUpdateDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isCompleted = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Todo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(TodoUpdateDto dto) {
        this.title = dto.title();
        this.content = dto.content();
    }

    public void toggleCompletion() {
        this.isCompleted = !this.isCompleted;
    }
}
