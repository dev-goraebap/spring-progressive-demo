package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminCommentIndexRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.CommentService;
import xyz.goraebap.spring_progressive_demo.infra.service.CommentQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentQueryService commentQueryService;
    private final CommentService commentService;

    @GetMapping
    public String index(@ModelAttribute("req") AdminCommentIndexRequest req, Model model) {
        var commentData = commentQueryService.getAdminCommentsWithPagination(
                req.getSearch(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("commentData", commentData);
        return "pages/admin/comments/index";
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        model.addAttribute("commentId", id);
        return "pages/admin/comments/remove";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "댓글이 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/comments")
                .build();
    }
}
