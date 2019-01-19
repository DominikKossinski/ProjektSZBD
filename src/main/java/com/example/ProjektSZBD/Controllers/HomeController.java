package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony głównej.
 */
@Controller
public class HomeController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony głównej.
     *
     * @return "home" - nazwa widoku strony głównej
     */
    @GetMapping("/home")
    public String getHello(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, auth);
        return "home";

    }
}
