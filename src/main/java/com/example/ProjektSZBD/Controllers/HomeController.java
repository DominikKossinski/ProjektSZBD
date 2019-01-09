package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (auth.getName().compareTo("anonymousUser") == 0) {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isPatient", false);
            model.addAttribute("isDoctor", false);
        } else {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("userId", auth.getName());
            Set<String> roles = auth.getAuthorities().stream()
                    .map(r -> r.getAuthority()).collect(Collectors.toSet());
            ArrayList<String> list = new ArrayList<>(roles);
            model.addAttribute("role", list.get(0));
            if (list.get(0).compareTo("ROLE_ADMIN") != 0 && list.get(0).compareTo("ROLE_PATIENT") != 0) {
                model.addAttribute("isDoctor", true);
                model.addAttribute("isPatient", false);
            } else if (list.get(0).compareTo("ROLE_PATIENT") == 0) {
                model.addAttribute("isPatient", true);
                model.addAttribute("isDoctor", false);
            } else {
                model.addAttribute("isPatient", false);
                model.addAttribute("isDoctor", false);
            }
        }
        return "home";

    }
}
