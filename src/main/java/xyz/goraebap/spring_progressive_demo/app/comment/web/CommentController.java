package xyz.goraebap.spring_progressive_demo.app.comment.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import xyz.goraebap.spring_progressive_demo.app.comment.CommentService;
import xyz.goraebap.spring_progressive_demo.app.comment.dto.CreateCommentRequest;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 목록 + 폼 조회 (hx-swap용)
     */
    @GetMapping("/{postSlug}")
    public String index(
            @PathVariable String postSlug,
            Model model
    ) {
        var comments = commentService.getComments(postSlug);
        int commentCount = comments.size();

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("requestId", commentService.generateRequestId());

        return "entities/comment/index";
    }

    /**
     * 댓글 등록
     */
    @PostMapping("/{postSlug}")
    public String create(
            @PathVariable String postSlug,
            @RequestParam String requestId,
            @Valid CreateCommentRequest request,
            Model model
    ) {
        commentService.create(requestId, postSlug, request);

        var comments = commentService.getComments(postSlug);
        int commentCount = comments.size();

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("requestId", commentService.generateRequestId());
        model.addAttribute("success", true);

        return "entities/comment/index";
    }
}
