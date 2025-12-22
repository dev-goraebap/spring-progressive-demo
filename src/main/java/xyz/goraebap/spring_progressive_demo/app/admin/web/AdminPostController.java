package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.client.dto.AdminPostIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;

@Controller("adminPostController")
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

    @GetMapping("/add")
    public String add() {
        return "pages/admin/posts/add";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var post = postQueryService.getPostById(id);
        model.addAttribute("post", post);
        return "pages/admin/posts/edit";
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        var post = postQueryService.getPostById(id);
        model.addAttribute("post", post);
        return "pages/admin/posts/remove";
    }
}
