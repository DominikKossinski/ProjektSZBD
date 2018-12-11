package com.example.ProjektSZBD.RestControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleRestController {

    @RequestMapping("/api/hello")
    public String getHello(@RequestParam(name = "text", required = false) String text) {
        if (text.isEmpty()) {
            return "Hello world!";
        } else {
            return text;
        }
    }
}
