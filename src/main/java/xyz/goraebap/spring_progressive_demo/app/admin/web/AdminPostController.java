package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.goraebap.spring_progressive_demo.app.admin.app.PostService;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostCreateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostUpdateRequest;
import xyz.goraebap.spring_progressive_demo.app.client.dto.AdminPostIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;

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
    public String addForm() {
        return "pages/admin/posts/add";
    }

    @PostMapping
    public String create(@Valid AdminPostCreateRequest req, RedirectAttributes redirectAttributes) {
        // TODO: 실제 인증된 사용자 ID로 교체
        Long userId = 1L;
        postService.create(req, userId);
        redirectAttributes.addFlashAttribute("success", "게시물이 등록되었습니다.");
        return "redirect:/admin/posts";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var post = postQueryService.getPostById(id);
        model.addAttribute("post", post);
        return "pages/admin/posts/edit";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid AdminPostUpdateRequest req, RedirectAttributes redirectAttributes) {
        postService.update(id, req);
        redirectAttributes.addFlashAttribute("success", "게시물이 수정되었습니다.");
        return "redirect:/admin/posts";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        postService.delete(id);
        redirectAttributes.addFlashAttribute("success", "게시물이 삭제되었습니다.");
        return "redirect:/admin/posts";
    }
}
