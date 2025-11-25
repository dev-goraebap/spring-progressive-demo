package xyz.goraebap.spring_progressive_demo.app.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPagesController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Spring Progressive Demo");
        return "pages/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Spring Progressive Demo");
        return "pages/about";
    }
}
