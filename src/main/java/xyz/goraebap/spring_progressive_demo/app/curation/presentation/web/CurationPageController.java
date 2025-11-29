package xyz.goraebap.spring_progressive_demo.app.curation.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/curations")
public class CurationPageController {

    @GetMapping
    public String index(Model model) {
        return "pages/curation/index";
    }
}
