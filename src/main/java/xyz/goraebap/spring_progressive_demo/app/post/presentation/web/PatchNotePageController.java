package xyz.goraebap.spring_progressive_demo.app.post.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patch-notes")
public class PatchNotePageController {

    @GetMapping
    public String index() {
        return "pages/patch-note/index";
    }
}
