package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostCreateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostUpdateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.PostService;
import xyz.goraebap.spring_progressive_demo.app.client.dto.AdminPostIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller("adminPostController")
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostQueryService postQueryService;
    private final PostService postService;

    @GetMapping
    public String index(@ModelAttribute("req") AdminPostIndexRequest req, Model model) {
        var postData = postQueryService.getAdminPostsWithPagination(
                req.getPostType(),
                req.getIsPublishedYn(),
                req.getTitle(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("postData", postData);
        return "pages/admin/posts/index";
    }

    @GetMapping("/add")
    public String add() {
        return "pages/admin/posts/add";
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid AdminPostCreateRequest req,
            @AuthenticationPrincipal Long userId
    ) {
        postService.create(req, userId);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "게시물이 등록되었습니다.")
                        .build())
                .header("HX-Redirect", "/admin/posts")
                .build();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var post = postQueryService.getPostById(id);
        model.addAttribute("post", post);
        return "pages/admin/posts/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid AdminPostUpdateRequest req
    ) {
        postService.update(id, req);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "게시물이 수정되었습니다.")
                        .build())
                .header("HX-Redirect", "/admin/posts")
                .build();
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        var post = postQueryService.getPostById(id);
        model.addAttribute("post", post);
        return "pages/admin/posts/remove";
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "게시물이 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/posts")
                .build();
    }
}
