package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony logowania.
 */
@Controller
public class LoginController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony logowania
     *
     * @return "login" - nazwa widoku strony logowania
     */
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
}
