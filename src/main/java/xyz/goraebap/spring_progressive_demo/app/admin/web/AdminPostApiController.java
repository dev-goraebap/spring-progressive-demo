package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostCreateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostUpdateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.PostService;

@RestController
@RequestMapping("/api/v1/admin/posts")
@RequiredArgsConstructor
public class AdminPostApiController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody AdminPostCreateRequest req,
            @AuthenticationPrincipal Long userId
    ) {
        postService.create(req, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminPostUpdateRequest req
    ) {
        postService.update(id, req);
        return ResponseEntity.ok().build();
    }
}
