package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.app.TagService;

@RestController
@RequestMapping("/api/v1/admin/tags")
@RequiredArgsConstructor
public class AdminTagApiController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TagCreateRequest request) {
        tagService.create(request.name());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody TagUpdateRequest request) {
        tagService.update(id, request.name());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.ok().build();
    }

    public record TagCreateRequest(String name) {}
    public record TagUpdateRequest(String name) {}
}
