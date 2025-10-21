package xyz.goraebap.spring_progressive_demo.app.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoCreateDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoUpdateDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.response.TodoResponseDto;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoResponseDto> findAll() {
        return todoRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(TodoCreateDto dto) {
        Todo todo = Todo.builder()
                .title(dto.title())
                .content(dto.content())
                .build();

        todoRepository.save(todo);
    }

    @Transactional
    public void update(Long id, TodoUpdateDto dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));

        todo.update(dto);
    }

    @Transactional
    public void updateCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        todo.toggleCompletion();
    }

    @Transactional
    public void destroy(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        todoRepository.delete(todo);
    }

    private TodoResponseDto toResponseDto(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .isCompleted(todo.isCompleted())
                .content(todo.getContent())
                .createdAt(todo.getCreatedAt())
                .build();
    }
}
