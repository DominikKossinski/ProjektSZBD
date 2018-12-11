package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExampleController {

    @GetMapping("/hello")
    public String getHello(Model model) {
        model.addAttribute("UserId", "1");
        return "hello.html";

    }
}
