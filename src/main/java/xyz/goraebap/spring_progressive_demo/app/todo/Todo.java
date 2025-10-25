package xyz.goraebap.spring_progressive_demo.app.todo;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoUpdateDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Getter
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

    public static Todo create(String title, String content) {
        Todo todo = new Todo();
        todo.title = title;
        todo.content = content;
        return todo;
    }

    public void update(TodoUpdateDto dto) {
        this.title = dto.title();
        this.content = dto.content();
    }

    public void toggleCompletion() {
        this.isCompleted = !this.isCompleted;
    }
}
