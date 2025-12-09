package xyz.goraebap.spring_progressive_demo.app.tag.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    @GetMapping
    public String index() {
        return "pages/admin/tags/index";
    }
}
