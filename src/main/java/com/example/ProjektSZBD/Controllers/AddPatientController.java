package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania pacjenta.
 */
@Controller
public class AddPatientController {

    /**
     * Metoda zwracająca nazwę widoku strony dodawania pacjenta.
     *
     * @param model - model widoku
     * @return "addPatient" - nazwa widoku strony.
     */
    @GetMapping("/addPatient")
    public String addPatient(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("id", auth.getName());
        return "addPatient";
    }
}
