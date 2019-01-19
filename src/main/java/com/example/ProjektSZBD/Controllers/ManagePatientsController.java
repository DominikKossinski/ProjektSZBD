package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania pacjentami.
 */
@Controller
public class ManagePatientsController {

    /**
     * Metoda zwracająca nazwę widoku strony zarządzania pacjentami.
     *
     * @param model - model widoku
     * @return "ManagePatients" - nazwa widoku strony.
     */
    @GetMapping("/managePatients")
    public String addPatient(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("id", auth.getName());
        return "ManagePatients";
    }
}
