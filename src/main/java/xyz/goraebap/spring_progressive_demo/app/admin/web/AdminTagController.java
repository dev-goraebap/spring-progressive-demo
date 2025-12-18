package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminTagIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.TagQueryService;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagQueryService tagQueryService;

    @GetMapping
    public String index(@ModelAttribute("req") AdminTagIndexRequest req, Model model) {
        var tagData = tagQueryService.getAdminTagsWithPagination(
                req.getName(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("tagData", tagData);
        return "pages/admin/tags/index";
    }

    @GetMapping("/new")
    public String newForm() {
        return "pages/admin/tags/new";
    }
}
