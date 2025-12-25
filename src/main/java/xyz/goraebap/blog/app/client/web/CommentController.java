package xyz.goraebap.blog.app.client.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.blog.contract.comment.CreateCommentDto;
import xyz.goraebap.blog.contract.comment.CommentCreator;
import xyz.goraebap.blog.infra.mapper.CommentViewMapper;

import java.util.UUID;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCreator commentCreator;
    private final CommentViewMapper commentViewMapper;

    @GetMapping("/{postSlug}")
    public String index(
            @PathVariable String postSlug,
            Model model
    ) {
        var comments = commentViewMapper.findByPostSlug(postSlug);

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("requestId", UUID.randomUUID().toString());

        return "entities/comment/index";
    }

    @PostMapping("/{postSlug}")
    public String create(
            @PathVariable String postSlug,
            @RequestParam String requestId,
            @Valid CreateCommentDto request,
            Model model
    ) {
        commentCreator.create(requestId, postSlug, request);

        var comments = commentViewMapper.findByPostSlug(postSlug);

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("requestId", UUID.randomUUID().toString());

        return "entities/comment/index";
    }
}
