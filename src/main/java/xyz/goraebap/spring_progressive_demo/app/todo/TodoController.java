package xyz.goraebap.spring_progressive_demo.app.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoCreateDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.TodoUpdateDto;
import xyz.goraebap.spring_progressive_demo.app.todo.dto.response.TodoResponseDto;

import java.util.List;

@Tag(name = "Todo", description = "Todo API")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Get all todos", description = "할 일 목록을 조회합니다.")
    @GetMapping
    public List<TodoResponseDto> index() {
        return todoService.findAll();
    }

    @Operation(summary = "Create todo", description = "새로운 할 일을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody TodoCreateDto dto) {
        todoService.create(dto);
    }

    @Operation(summary = "Update todo", description = "기존의 할 일을 변경합니다.")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody TodoUpdateDto dto) {
        todoService.update(id, dto);
    }

    @Operation(summary = "Update todo complete", description = "기존 할 일의 완료상태를 변경합니다.")
    @PatchMapping("/{id}/toggle/completion")
    public void updateCompleted(@PathVariable Long id) {
        todoService.updateCompleted(id);
    }

    @Operation(summary = "Delete todo", description = "기존의 할 일을 제거합니다.")
    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Long id) {
        todoService.destroy(id);
    }
}
