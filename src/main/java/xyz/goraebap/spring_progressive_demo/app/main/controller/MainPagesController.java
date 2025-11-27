package xyz.goraebap.spring_progressive_demo.app.main.controller;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.goraebap.spring_progressive_demo.app.main.dto.MainIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.mapper.PostViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPagesController {

    private final PostViewMapper postViewMapper;

    @GetMapping("/")
    public String index(Model model, @ParameterObject MainIndexRequest dto) {
        List<PostViewModel> posts = postViewMapper.findPosts(dto.getOrderType());
        model.addAttribute("posts", posts);
        return "pages/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Spring Progressive Demo");
        return "pages/about";
    }
}
