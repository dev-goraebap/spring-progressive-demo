package xyz.goraebap.spring_progressive_demo.app.post.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.post.dto.AdminPostIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;

@Controller
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostQueryService postQueryService;

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
}
