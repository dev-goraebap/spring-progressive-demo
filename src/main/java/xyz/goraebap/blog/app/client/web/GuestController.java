package xyz.goraebap.blog.app.client.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    @GetMapping("/info")
    public String info(
            @RequestParam(defaultValue = "default") String permission,
            Model model
    ) {
        model.addAttribute("permission", permission);
        return "pages/client/guest/info";
    }
}
