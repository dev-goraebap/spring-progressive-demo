package xyz.goraebap.spring_progressive_demo.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;

    @GetMapping("/{slug}")
    public String show(
            @PathVariable String slug,
            Model model
    ) {
        var post = postQueryService.getPostBySlug(slug);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("post", post);
        return "pages/post/show";
    }
}
