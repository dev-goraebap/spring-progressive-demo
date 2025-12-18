package xyz.goraebap.spring_progressive_demo.app.client.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.client.dto.CreateCommentRequest;
import xyz.goraebap.spring_progressive_demo.contract.comment.CommentCreator;
import xyz.goraebap.spring_progressive_demo.infra.mapper.CommentViewMapper;

import java.util.UUID;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCreator commentCreator;
    private final CommentViewMapper commentViewMapper;

    /**
     * 댓글 목록 + 폼 조회 (hx-swap용)
     */
    @GetMapping("/{postSlug}")
    public String index(
            @PathVariable String postSlug,
            Model model
    ) {
        var comments = commentViewMapper.findByPostSlug(postSlug);
        int commentCount = comments.size();

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("requestId", UUID.randomUUID().toString());

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
        commentCreator.create(requestId, postSlug, request);

        var comments = commentViewMapper.findByPostSlug(postSlug);
        int commentCount = comments.size();

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("requestId", UUID.randomUUID().toString());
        model.addAttribute("success", true);

        return "entities/comment/index";
    }
}
