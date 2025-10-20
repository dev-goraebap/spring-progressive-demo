package xyz.goraebap.spring_progressive_demo.app.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @GetMapping
    public String index() {
        return "";
    }
}
