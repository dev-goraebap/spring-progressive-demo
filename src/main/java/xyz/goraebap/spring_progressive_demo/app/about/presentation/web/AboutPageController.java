package xyz.goraebap.spring_progressive_demo.app.about.presentation.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutPageController {

    @GetMapping
    public String index() {
        return "pages/about/index";
    }
}
