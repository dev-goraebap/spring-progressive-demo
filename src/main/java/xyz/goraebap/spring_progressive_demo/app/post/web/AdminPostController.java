package xyz.goraebap.spring_progressive_demo.app.post.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    @GetMapping
    public String index() {
        return "pages/admin/posts/index";
    }
}
