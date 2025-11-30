package xyz.goraebap.spring_progressive_demo.app.post.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostPageController {
    @GetMapping("/{slug}")
    public String show(Model model) {
        return "";
    }
}
