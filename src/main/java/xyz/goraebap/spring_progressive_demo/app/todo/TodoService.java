package xyz.goraebap.spring_progressive_demo.app.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoCreateDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoSearchDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoUpdateDto;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoQueryMapper todoQueryMapper;
    private final TodoRepository todoRepository;

    /**
     * 동적 조건으로 Todo 목록 조회
     */
    public List<Todo> findTodos(TodoSearchDto searchDto) {
        return todoQueryMapper.findWithCursor(
                searchDto.title(),
                searchDto.isCompleted(),
                searchDto.cursor(),
                searchDto.sortOrder(),
                searchDto.perPage()
        );
    }

    @Transactional
    public void create(TodoCreateDto dto) {
        Todo todo = Todo.create(dto.title(), dto.content());
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
}
