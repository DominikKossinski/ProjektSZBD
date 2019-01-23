package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony wyświetlania pobytów pacjenta.
 */
@Controller
public class MyStaysController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony wyświetlania pobytów pacjenta.
     *
     * @param model - model widoku
     * @return "addIllness" - nazwa widoku strony wyświetlania pobytów pacjenta
     */
    @GetMapping("/myStays")
    public String getMyStays(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("pesel", authentication.getName());
        getUserRoles(model, authentication);
        return "MyStays";
    }
}
