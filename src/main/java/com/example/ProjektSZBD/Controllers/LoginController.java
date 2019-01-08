package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony logowania.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
}
