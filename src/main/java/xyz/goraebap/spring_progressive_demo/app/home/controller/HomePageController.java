package xyz.goraebap.spring_progressive_demo.app.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Spring Progressive Demo");
        return "pages/index";
    }
}
